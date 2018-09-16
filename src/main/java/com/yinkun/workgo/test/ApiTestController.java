package com.yinkun.workgo.test;

import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import com.yinkun.workgo.test.kit.HttpUtils;
import com.yinkun.workgo.test.kit.SignKit;

import java.util.HashMap;
import java.util.Map;

public class ApiTestController extends Controller {

    public void test() {
        String target = null, sk = null;
        Map targetParams = new HashMap();
        Map<String, String[]> map = getParaMap();

        StringBuilder report = new StringBuilder();

        if (map.size() <= 0) {
            renderText("请输入target测试地址及相关参数");
            return;
        }

        for (String key : map.keySet()) {
            String [] params = map.get(key);

            if ("target".equals(key.toLowerCase())) {
                target = params[0];
            }
            else if ("sk".equals(key)) {
                sk = params[0];
            }
            else if ("nonceStr".equals(key) && params[0].equals("*")) {
                targetParams.put("nonceStr", SignKit.getUUID32());
            }
            else {
                for (String p : params) {
                    targetParams.put(key, p);
                }
            }
        }

        if (null == target) {
            renderText("没有API目标,请填写target参数");
            return;
        }

        if (null == sk) {
            renderText("请输入秘钥");
            return;
        }

        // 去掉target进行签名.
        targetParams.put("sign", SignKit.getSign(targetParams, sk));
        report.append(targetParams.toString());

        if (null != target) {
            try {
                String result = HttpUtils.post(target, targetParams);
                //renderJson(Ret.ok("report", report.toString()).set("result", result));
                renderJson(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}