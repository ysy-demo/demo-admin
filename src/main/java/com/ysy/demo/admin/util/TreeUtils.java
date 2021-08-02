package com.ysy.demo.admin.util;

import com.ysy.demo.admin.dto.BaseTreeNodeRes;
import com.ysy.demo.admin.dto.BaseTreeRes;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TreeUtils {

    private final static Long TOP_NODE_ID = 0L;

    public static <D, T extends BaseTreeNodeRes> BaseTreeRes<T> build(List<D> data, Function<D, T> function) {
        if (data == null || data.isEmpty()) {
            return BaseTreeRes.EMPTY;
        }
        Map<Long, List<BaseTreeRes<T>>> pidNodeMap = data.stream()
                .map(e -> BaseTreeRes.<T>builder().node(function.apply(e)).build())
                .collect(Collectors.groupingBy(e -> e.getNode().getPid()));
        List<BaseTreeRes<T>> topNodes = pidNodeMap.get(TOP_NODE_ID);
        setChild(topNodes, pidNodeMap);
        return BaseTreeRes.<T>builder().child(topNodes).build();
    }

    private static <T extends BaseTreeNodeRes> void setChild(
            List<BaseTreeRes<T>> nodes, Map<Long, List<BaseTreeRes<T>>> pidNodeMap) {
        for (BaseTreeRes<T> t : nodes) {
            List<BaseTreeRes<T>> child = pidNodeMap.get(t.getNode().getId());
            t.setChild(child);
            if (child != null) {
                setChild(child, pidNodeMap);
            }
        }
    }
}