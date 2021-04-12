package com.app.pack0311;

public class Application {

    public static void main(String[] args) {
         String q = "agree_afa_engine_lsr_http_pending_tasks{abc=\"\",instance=\"$instance\",efg=\"\"}";
        // String q = "agree_afa_engine_lsr_http_pending_tasks{abc=\"\",instance=\"$instance\",efg=\"\"}";
        // String q = "agree_afa_engine_lsr_http_pending_tasks{abc=\"\",instance=\"$instance\"}";
        // String q = "agree_afa_engine_lsr_http_pending_tasks{instance=\"$instance\"}";
        while (q.contains("$")) {
            int $index = q.indexOf("$");
            int left = $index;
            for (; left >= 0; left--) {
                if (q.charAt(left) == ',' || q.charAt(left) == '{') break;
            }
            left++;
            int right = $index;
            for (; right < q.length(); right++) {
                if (q.charAt(right) == ',' || q.charAt(right) == '}') break;
            }
            if (q.charAt(right) == ',') right++;
            q = q.substring(0, left) + q.substring(right);
        }
        System.out.println(q);
    }
}
