package top.liewyoung.strategy.gameEvent;

import top.liewyoung.strategy.TitlesTypes;

import java.util.*;

/**
 * 事件注册表
 * 管理所有游戏事件的注册、注销和随机选择
 * 
 * @author LiewYoung
 * @since 2025/12/18
 */
public class EventRegistry {

    private final Map<TitlesTypes, List<GameEvent>> events = new HashMap<>();

    public EventRegistry() {
        // 初始化所有事件类型的列表
        for (TitlesTypes type : TitlesTypes.values()) {
            events.put(type, new ArrayList<>());
        }
    }

    /**
     * 注册单个事件
     *
     * @param event 活动
     */
    public void register(GameEvent event) {
        TitlesTypes type = event.getType();
        if (type != null && events.containsKey(type)) {
            events.get(type).add(event);
        }
    }

    /**
     * 批量注册事件
     * 多参数 可变参数
     */
    public void registerAll(GameEvent... eventList) {
        for (GameEvent event : eventList) {
            register(event);
        }
    }

    /**
     * 注销事件
     */
    public void unregister(GameEvent event) {
        TitlesTypes type = event.getType();
        if (type != null && events.containsKey(type)) {
            events.get(type).remove(event);
        }
    }

    /**
     * 获取指定类型的所有事件
     */
    public List<GameEvent> getEvents(TitlesTypes type) {
        return events.getOrDefault(type, Collections.emptyList());
    }

    /**
     * 随机获取指定类型的一个事件
     * 
     * @return 随机选中的事件，如果该类型没有事件则返回null
     */
    public GameEvent getRandomEvent(TitlesTypes type, Random random) {
        List<GameEvent> typeEvents = events.get(type);
        if (typeEvents == null || typeEvents.isEmpty()) {
            return null;
        }
        return typeEvents.get(random.nextInt(typeEvents.size()));
    }

    /**
     * 获取指定类型的事件数量
     */
    public int getEventCount(TitlesTypes type) {
        List<GameEvent> typeEvents = events.get(type);
        return typeEvents != null ? typeEvents.size() : 0;
    }

    /**
     * 获取所有事件总数
     */
    public int getTotalEventCount() {
        return events.values().stream().mapToInt(List::size).sum();
    }

    /**
     * 清空所有事件
     */
    public void clear() {
        for (List<GameEvent> list : events.values()) {
            list.clear();
        }
    }
}
