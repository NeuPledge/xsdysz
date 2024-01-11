package cn.iocoder.yudao.module.game.util;


import cn.iocoder.yudao.module.game.service.console.clients.dst.model.SteamModInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SteamUtil {

    public static SteamModInfo getModInfo(String modId) {
        String link = "https://steamcommunity.com/sharedfiles/filedetails/?id=" + modId;
        Map<String, String> header = new HashMap<>();
        header.put("Accept-Language", "zh-CN,zh");// 查询中文.
        String html = OKHttpUtil.sendGet(link, null, header);
        Assert.notNull(html, link + "解析模组信息失败.");
        Document doc = Jsoup.parse(html);
        SteamModInfo steamModInfo = new SteamModInfo();
        steamModInfo.setModId(modId);
        steamModInfo.setUrl(link);

        Elements titleElements = doc.selectXpath("//div[@class='workshopItemTitle']");
        if (titleElements.size() != 0) {
            steamModInfo.setTitle(titleElements.text());
        } else {
            steamModInfo.setStatus(0);
            return steamModInfo;
        }
//        Elements versionElements = doc.selectXpath("/html/body/div[1]/div[7]/div[5]/div[1]/div[4]/div[10]/div/div[2]/div[2]/div/a");
        Elements versionElements = doc.selectXpath("//div[@class='workshopTags']/a");
        if (versionElements.size() != 0) {
            steamModInfo.setVersion(versionElements.text());
        } else {
            versionElements = doc.selectXpath("/html/body/div[1]/div[7]/div[5]/div[1]/div[4]/div[10]/div/div[2]/div[1]/div/a");
            if (versionElements.size() != 0) {
                steamModInfo.setVersion(versionElements.text());
            }
        }

        Elements authorElements = doc.selectXpath("//div[@class='breadcrumbs']/a[3]");
        if (authorElements.size() != 0) {
            steamModInfo.setAuthor(authorElements.text().replaceAll("的创意工坊", ""));
        }

        Elements imageElements = doc.selectXpath("//*[@id='previewImageMain']");
        if (imageElements.size() != 0) {
            String image = imageElements.get(0).attr("src");
            steamModInfo.setImage(image);
        } else {
            imageElements = doc.selectXpath("/html/body/div[1]/div[7]/div[5]/div[1]/div[4]/div[10]/div/div[1]/div[1]/div/div/a/img");
            if (imageElements.size() != 0) {
                String image = imageElements.get(0).attr("src");
                steamModInfo.setImage(image);
            }
        }

        Elements starElements = doc.selectXpath("//div[@class='fileRatingDetails']/img");
        if (starElements.size() != 0) {
            String image = starElements.get(0).attr("src");
            steamModInfo.setStarUrl(image);
        }

        Elements tagsElements = doc.selectXpath("//div[@class='rightDetailsBlock']/a");
//        Elements tagsElements = doc.selectXpath("/html/body/div[1]/div[7]/div[5]/div[1]/div[4]/div[10]/div/div[2]/div[2]/a");
        if (tagsElements.size() != 0) {
            steamModInfo.setTags(tagsElements.text());
            if (tagsElements.text().contains("client_only_mod")) {
                steamModInfo.setIsServerMod(0);
            } else {
                steamModInfo.setIsServerMod(1);
            }
        } else {
            tagsElements = doc.selectXpath("/html/body/div[1]/div[7]/div[5]/div[1]/div[4]/div[10]/div/div[2]/div[1]/a");
            if (tagsElements.size() != 0) {
                steamModInfo.setTags(tagsElements.text());
                if (tagsElements.text().contains("client_only_mod")) {
                    steamModInfo.setIsServerMod(0);
                } else {
                    steamModInfo.setIsServerMod(1);
                }
            }
        }

        Elements detailsLeftElements = doc.selectXpath("//div[@class='detailsStatsContainerLeft']/div");
        Elements detailsRightElements = doc.selectXpath("//div[@class='detailsStatsContainerRight']/div");
//        Elements detailsLeftElements = doc.selectXpath("/html/body/div[1]/div[7]/div[5]/div[1]/div[4]/div[10]/div/div[2]/div[4]/div[1]/div");
//        Elements detailsRightElements = doc.selectXpath("/html/body/div[1]/div[7]/div[5]/div[1]/div[4]/div[10]/div/div[2]/div[4]/div[2]/div");

        // 1991746508 依赖1个模组
        // 1780226102 依赖2个模组
        // 1378549454 不依赖模组
        Elements dependModsElements = doc.selectXpath("/html/body/div[1]/div[7]/div[5]/div[1]/div[4]/div[11]/div[1]/div/div[1]/div[3]/a");
        if (dependModsElements.size() != 0) {
            List<String> dependMods = new ArrayList<>();
            for (int i = 0; i < dependModsElements.size(); i++) {
                Element element = dependModsElements.get(i);
                String href = element.attr("href");
                // https://steamcommunity.com/workshop/filedetails/?id=1378549454
                // ↓↓↓
                // 1378549454
                href = href.replace("https://steamcommunity.com/workshop/filedetails/?id=", "");
                dependMods.add(href);
            }
            steamModInfo.setDependMods(dependMods);
        }

        for (int i = 0; i < detailsLeftElements.size(); i++) {
            Element element = detailsLeftElements.get(i);
            String text = element.text();
            if (text.contains("文件大小")) {
                Element rightElement = detailsRightElements.get(i);
                String rightValue = rightElement.text();
                rightValue = rightValue.replaceAll("\n", "");
                steamModInfo.setFilesize(rightValue);
            } else if (text.contains("发表于")) {
                Element rightElement = detailsRightElements.get(i);
                String rightValue = rightElement.text();
                rightValue = rightValue.replaceAll("\n", "");
                steamModInfo.setModPublishTime(rightValue);
            } else if (text.contains("更新日期")) {
                Element rightElement = detailsRightElements.get(i);
                String rightValue = rightElement.text();
                rightValue = rightValue.replaceAll("\n", "");
                steamModInfo.setModUpdateTime(rightValue);
            }
        }
        steamModInfo.setStatus(1);
        return steamModInfo;
    }

    // 解析创意工坊的分页页面
    public static List<String> dstWorkshopPaginationParser(Integer page) {
        // https://steamcommunity.com/workshop/browse/?appid=322330&browsesort=totaluniquesubscribers&section=readytouseitems&actualsort=totaluniquesubscribers&p=3

        String pageUrl = "https://steamcommunity.com/workshop/browse/?appid=322330&browsesort=totaluniquesubscribers&section=readytouseitems&actualsort=totaluniquesubscribers&p=" + page;
        Map<String, String> header = new HashMap<>();
        header.put("Accept-Language", "zh-CN,zh");// 查询中文.
        String html = OKHttpUtil.sendGet(pageUrl, null, header);
        Assert.notNull(html, pageUrl + "解析模组信息失败.");
        Document doc = Jsoup.parse(html);

        // 模组链接
        Elements modUrlElements = doc.selectXpath("/html/body/div[1]/div[7]/div[2]/div[3]/div/div[3]/div/div[3]/div[*]/a[2]");
        List<String> modIds = new ArrayList<>();
        if (modUrlElements.size() != 0) {
            for (int i = 0; i < modUrlElements.size(); i++) {
                Element element = modUrlElements.get(i);
                String href = element.attr("href");
                // https://steamcommunity.com/sharedfiles/filedetails/?id=2321974509&searchtext=
                // ↓↓↓
                // 2321974509
                href = href.replace("https://steamcommunity.com/sharedfiles/filedetails/?id=", "");
                href = href.replace("&searchtext=", "");
                modIds.add(href);
            }
        }
        return modIds;
    }

    public static void main(String[] args) {
//        SteamModInfo modContent = SteamUtil.getModContent("https://steamcommunity.com/sharedfiles/filedetails/?id=2505341606");
//        SteamModInfo modContent = SteamUtil.getModContent("https://www.google.com.hk?id=1233");
        // 1991746508 依赖1个模组
        // 1780226102 依赖2个模组
        // 1378549454 不依赖模组
//        SteamModInfo modContent = SteamUtil.getModInfo("1378549454");
////        SteamModInfo modContent = SteamUtil.getModContent("https://steamcommunity.com/sharedfiles/filedetails/?id=2505341606");
//        System.out.println(JSON.toJSONString(modContent, true));

//        testDstWorkshopPageParser();
    }

}
