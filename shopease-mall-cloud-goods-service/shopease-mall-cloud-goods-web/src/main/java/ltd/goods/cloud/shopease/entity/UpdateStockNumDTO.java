 
package ltd.goods.cloud.shopease.entity;

import lombok.Data;
import java.util.List;

@Data
public class UpdateStockNumDTO {

    private List<StockNumDTO> stockNumDTOS;

    public List<StockNumDTO> getStockNumDTOS() {
        return stockNumDTOS;
    }

    public void setStockNumDTOS(List<StockNumDTO> stockNumDTOS) {
        this.stockNumDTOS = stockNumDTOS;
    }
}
