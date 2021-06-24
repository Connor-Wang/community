package com.wcaaotr.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Connor
 * @create 2021-06-22-16:18
 */
@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);
    // 替换符
    private static final String REPLACEMENT = "***";

    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init(){
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ){
            String keyword;
            while((keyword = reader.readLine()) != null){
                // 添加到前缀树
                this.addKeyword(keyword);
            }
        } catch (Exception e) {
            logger.error("加载敏感词文件失败：" + e.getMessage());
        }
    }

    /**
     * 将敏感词添加到前缀树
     * @param keyword 敏感词
     */
    private void addKeyword(String keyword){
        TrieNode currentNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = currentNode.getSubNode(c);
            if(subNode == null){
                // 初始化子节点
                subNode = new TrieNode();
                currentNode.addSubNode(c, subNode);
            }
            currentNode = subNode;
            // 设置结束标识
            if(i == keyword.length()-1){
                currentNode.setKeyWordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text){
        System.out.println("filter -> " + text);
        if(StringUtils.isBlank(text)){
            return null;
        }
        TrieNode currentNode = rootNode;
        int beginIndex = 0;
        int endIndex = 0;
        StringBuilder result = new StringBuilder();

        while(endIndex < text.length()){
            char c = text.charAt(endIndex);
            // 跳过特殊符号
            if(isSymbol(c)){
                // 当前节点是根节点时，将此符号计入结果，且 begin Index + 1
                if(currentNode == rootNode){
                    result.append(c);
                    beginIndex++;
                }
                endIndex++;
                continue;
            }
            // 检查下级节点
            currentNode = currentNode.getSubNode(c);
            if(currentNode == null){
                // 以 beginIndex 开始的字符不是敏感词
                result.append(text.charAt(beginIndex));
                endIndex = ++beginIndex;
                currentNode = rootNode;
            } else if (currentNode.isKeyWordEnd()) {
                // 发现敏感词
                result.append(REPLACEMENT);
                beginIndex = ++endIndex;
                currentNode = rootNode;
            } else {
                endIndex++;
            }
        }
        result.append(text.substring(beginIndex));
        return result.toString();
    }

    /**
     * 判断是否为符号
     * @param c 要判断的符号
     * @return 判断结果
     */
    private boolean isSymbol(Character c) {
        // 0x2E80~0x9FFF 是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    // 前缀树
    private class TrieNode{
        // 词结束标识
        private boolean isKeyWordEnd = false;
        // 子节点 key - 下级字符， value - 下级节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            this.isKeyWordEnd = keyWordEnd;
        }

        public void addSubNode(Character c, TrieNode node){
            subNodes.put(c, node);
        }

        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }
}
