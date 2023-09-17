package cn.iocoder.yudao.module.im.controller.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/im/hello")
public class ImController {

    @GetMapping("/echo")
    public String echo() {
        return "success";
    }

}
