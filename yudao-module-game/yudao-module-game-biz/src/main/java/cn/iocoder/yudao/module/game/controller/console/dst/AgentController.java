package cn.iocoder.yudao.module.game.controller.console.dst;


import com.alibaba.fastjson.JSON;
import com.dooyo.annotations.NoAuth;
import cn.iocoder.yudao.module.game.common.ResultData;
import cn.iocoder.yudao.module.game.common.params.Heartbeat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Agent管理")
@RequestMapping(value = "/agent")
public class AgentController {



    @NoAuth
    @Operation(summary = "上报心跳")
    @PostMapping("/heartbeat")
    public ResultData heartbeat(
        @RequestBody Heartbeat heartbeat
    ) {
        log.info(JSON.toJSONString(heartbeat));
        return ResultData.success();
    }
//
//    @NoAuth
//    @Operation(summary = "获取本机IP")
//    @GetMapping("/ip")
//    public String getIp(HttpServletRequest request){
//        String ip = request.getRemoteAddr();
//        return ip;
//    }
}
