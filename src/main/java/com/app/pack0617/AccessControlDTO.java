package com.app.pack0617;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AccessControlDTO {
    private static final String PARENTHESIS_LEFT = "(";
    private static final String PARENTHESIS_RIGHT = ")";

    private String label;
    private Comparison comparison;
    private String labelValue;
    private Relevance relevance;
    private List<AccessControlDTO> children;


    public static String toExpString(List<AccessControlDTO> list) {
        StringBuilder sb = new StringBuilder();
        for (AccessControlDTO ac : list) {
            if (ac.comparison != null) {
                sb.append("(").append(ac.label).append(ac.comparison.fun.conversionToString(ac.labelValue)).append(")");
            }
            if (ac.relevance != null) {
                sb.append(ac.relevance.val);
            }
            if (ac.children != null) {
                sb.append("(").append(toExpString(ac.children)).append(")");
            }
        }
        return sb.toString();
    }

    public static List<AccessControlDTO> explainExp(String s) {
        JSONArray array = resolveExpr(s);
        return explainExp(array);
    }

    private static List<AccessControlDTO> explainExp(JSONArray array) {
        List<AccessControlDTO> list = new ArrayList<>();
        for (Object obj : array) {
            AccessControlDTO dto = new AccessControlDTO();
            if (obj instanceof String) {
                String os = (String) obj;
                if (os.equals(Relevance.AND.val)) {
                    dto.setRelevance(Relevance.AND);
                } else if (os.equals(Relevance.OR.val)) {
                    dto.setRelevance(Relevance.OR);
                } else {
                    if (os.contains("!=")) {
                        dto.setComparison(Comparison.NOT_EQUAL);
                    } else if (os.contains("!=[")) {
                        dto.setComparison(Comparison.NOT_IN);
                    } else if (os.contains("=[")) {
                        dto.setComparison(Comparison.IN);
                    } else if (os.contains("-")) {
                        dto.setComparison(Comparison.REGEX);
                    } else {
                        dto.setComparison(Comparison.EQUAL);
                    }
                    dto.getComparison().fun.parse(os, dto);
                }
            } else if (obj instanceof JSONArray) {
                dto.children = explainExp((JSONArray) obj);
            }
            list.add(dto);
        }
        return list;
    }

    private static List<String> splitExpr(String exp) {
        List<String> list = new ArrayList<>();
        String temp = "";
        exp = exp.replace(" ", "");
        for (int i = 0; i < exp.length(); i++) {
            String str = exp.substring(i, i + 1);
            if (str.equals(PARENTHESIS_LEFT)) {
                if (PARENTHESIS_LEFT.equals(temp)) {//连续左括号，把前一个左括号放入list,temp不变
                    list.add(PARENTHESIS_LEFT);
                } else if (!temp.isEmpty()) {//不为空，添加temp到list
                    list.add(temp);
                    temp = PARENTHESIS_LEFT;
                } else {//temp为空，第一个左括号
                    temp = PARENTHESIS_LEFT;
                }
            } else if (str.equals(PARENTHESIS_RIGHT)) {
                if (!temp.isEmpty()) {//不为空
                    list.add(temp + str);
                } else {
                    list.add(str);
                }
                temp = "";
            } else {
                temp += str;
            }
        }
        if (!temp.isEmpty()) {
            list.add(temp);
        }
        return list;
    }

    private static JSONArray resolveExpr(List<String> expList, JSONArray parent) {
        JSONArray array = new JSONArray();
        int leftCount = 0;
        int rightCount = 0;
        List<Integer> indexs = new ArrayList<>();
        for (int i = 0; i < expList.size(); i++) {//进行循环，找出括号角标，左括号正值，右括号负值
            String str = expList.get(i);
            if (str.equals(PARENTHESIS_LEFT)) {
                indexs.add(i);
                leftCount++;
            } else if (str.equals(PARENTHESIS_RIGHT)) {
                indexs.add(-i);
                rightCount++;
            }
        }
        if (leftCount != rightCount) {
            throw new RuntimeException("表达式非法，左右括号数不一致");
        }
        List<IndexBean> indexTree = new ArrayList<>();
        //通过循环找到所有最外层的括号对角标
        while (indexs.size() != 0) {
            leftCount = 0;
            rightCount = 0;
            int firstLIndex = 999;
            int lastRIndex = 999;
            for (int index : indexs) {
                if (index >= 0 && firstLIndex == 999) {//设置初始值
                    firstLIndex = index;
                    leftCount++;
                } else if (index < 0) {
                    rightCount++;
                    //匹配上括号
                    if (leftCount == rightCount) {//从左到右遍历，当第一个左括号偏移等于右括号时，判断为同一组
                        lastRIndex = index;
                        break;
                    }
                } else {
                    leftCount++;
                }
            }
            //找到一组外层括号，切割掉包含部分，重新寻找下一组
            if (firstLIndex != 999 && lastRIndex != 999) {
                indexTree.add(new IndexBean(firstLIndex, Math.abs(lastRIndex)));
                indexs = indexs.subList(leftCount + rightCount, indexs.size());
            } else {
                throw new RuntimeException("表达式错误，未匹配到右括号");
            }
        }
        int treeIndex = 0;//indexbean角标
        for (int i = 0; i < expList.size(); i++) {
            boolean isContains = false;
            for (IndexBean bean : indexTree) {
                if (i >= bean.getStart() && i <= bean.getEnd()) {
                    isContains = true;//在括号范围内
                    break;
                }
            }
            if (!isContains) {//不在括号范围内直接添加
                array.add(expList.get(i));
            } else {
                IndexBean indexBean = indexTree.get(treeIndex);
                if (i == indexBean.getStart()) {//位于当前index的start时，为括号内内容，切出括号内内容并递归处理括号内内容
                    if (indexTree.size() != treeIndex + 1) {//index位移
                        treeIndex++;
                    }
                    //切出括号内内容并递归处理
                    List<String> subExpList = expList.subList(indexBean.getStart() + 1, indexBean.getEnd());
                    resolveExpr(subExpList, array);
                }
            }
        }
        if (parent != null) {
            parent.add(array);
        }
        return array;
    }

    private static JSONArray resolveExpr(String exp) {
        return resolveExpr(splitExpr(exp), null);
    }

    public enum Comparison {

        EQUAL("=", new ComparisonFun() {
            @Override
            public String conversionToString(String labelValue) {
                return EQUAL.val + labelValue;
            }

            @Override
            public void parse(String os, AccessControlDTO dto) {
                int i = os.indexOf("=");
                dto.setLabel(os.substring(1, i));
                dto.setComparison(Comparison.EQUAL);
                dto.setLabelValue(os.substring(i + 1, os.length() - 1));
            }
        }),
        NOT_EQUAL("!=", new ComparisonFun() {
            @Override
            public String conversionToString(String labelValue) {
                return NOT_EQUAL.val + labelValue;
            }

            @Override
            public void parse(String os, AccessControlDTO dto) {
                int i = os.indexOf("!=");
                dto.setLabel(os.substring(1, i));
                dto.setComparison(Comparison.NOT_EQUAL);
                dto.setLabelValue(os.substring(i + 2, os.length() - 1));
            }
        }),
        IN("[]", new ComparisonFun() {
            @Override
            public String conversionToString(String labelValue) {
                return "=[" + labelValue + "]";
            }

            @Override
            public void parse(String os, AccessControlDTO dto) {
                int i = os.indexOf("=[");
                dto.setLabel(os.substring(1, i));
                dto.setComparison(Comparison.IN);
                dto.setLabelValue(os.substring(i + 2, os.length() - 2));
            }
        }),
        NOT_IN("![]", new ComparisonFun() {
            @Override
            public String conversionToString(String labelValue) {
                return "=![" + labelValue + "]";
            }

            @Override
            public void parse(String os, AccessControlDTO dto) {
                int i = os.indexOf("!=[");
                dto.setLabel(os.substring(1, i));
                dto.setComparison(Comparison.NOT_IN);
                dto.setLabelValue(os.substring(i + 3, os.length() - 2));
            }
        }),
        REGEX("-", new ComparisonFun() {
            @Override
            public String conversionToString(String labelValue) {
                return "-" + labelValue + "~";
            }

            @Override
            public void parse(String os, AccessControlDTO dto) {
                int i = os.indexOf("-");
                dto.setLabel(os.substring(1, i));
                dto.setComparison(Comparison.REGEX);
                dto.setLabelValue(os.substring(i + 1, os.length() - 2));
            }
        });

        public String val;
        public ComparisonFun fun;

        Comparison(String val, ComparisonFun fun) {
            this.val = val;
            this.fun = fun;
        }
    }

    public enum Relevance {
        AND("&&"), OR("||");

        public String val;

        Relevance(String val) {
            this.val = val;
        }
    }

    public interface ComparisonFun {
        String conversionToString(String labelValue);

        void parse(String s, AccessControlDTO dto);
    }

    public static void main(String[] args) {
        List<AccessControlDTO> list = explainExp("" +
                "(systemcode=123)||(servicecode=![svc-13YbgNGX,svc-1bN00zBO])||(ip=[10.8.4,2.36])"
        );
    }

    static class IndexBean {
        private int start;
        private int end;

        public IndexBean(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }
    }

}
