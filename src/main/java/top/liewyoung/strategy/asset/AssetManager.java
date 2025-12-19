package top.liewyoung.strategy.asset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * 资产管理器
 * 管理玩家的所有资产
 * 
 * @author LiewYoung
 * @since 2025/12/18
 */
public class AssetManager {

    private final List<Asset> assets = new ArrayList<>();
    private Consumer<Asset> onAssetAdded;
    private Consumer<Asset> onAssetRemoved;
    private Runnable onAssetsUpdated;

    /**
     * 添加资产
     */
    public void addAsset(Asset asset) {
        assets.add(asset);
        if (onAssetAdded != null) {
            onAssetAdded.accept(asset);
        }
    }

    /**
     * 移除资产
     */
    public void removeAsset(Asset asset) {
        assets.remove(asset);
        if (onAssetRemoved != null) {
            onAssetRemoved.accept(asset);
        }
    }

    /**
     * 获取所有资产（只读）
     */
    public List<Asset> getAssets() {
        return Collections.unmodifiableList(assets);
    }

    /**
     * 更新所有资产价值
     */
    public void updateAllAssets(Random random) {
        for (Asset asset : assets) {
            asset.updateValue(random);
        }
        if (onAssetsUpdated != null) {
            onAssetsUpdated.run();
        }
    }

    /**
     * 获取资产总价值
     */
    public int getTotalValue() {
        return assets.stream().mapToInt(Asset::getCurrentValue).sum();
    }

    /**
     * 获取总月收入
     */
    public int getTotalMonthlyIncome() {
        return assets.stream().mapToInt(Asset::getMonthlyIncome).sum();
    }

    /**
     * 获取资产数量
     */
    public int getAssetCount() {
        return assets.size();
    }

    /**
     * 按类型获取资产
     */
    public List<Asset> getAssetsByType(AssetType type) {
        return assets.stream()
                .filter(a -> a.getType() == type)
                .toList();
    }

    /**
     * 清空所有资产
     */
    public void clear() {
        assets.clear();
        if (onAssetsUpdated != null) {
            onAssetsUpdated.run();
        }
    }



    public void setOnAssetAdded(Consumer<Asset> callback) {
        this.onAssetAdded = callback;
    }

    public void setOnAssetRemoved(Consumer<Asset> callback) {
        this.onAssetRemoved = callback;
    }

    public void setOnAssetsUpdated(Runnable callback) {
        this.onAssetsUpdated = callback;
    }
}
