package seek.util.common.config.object.common.common.object.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeAmountDTO {
    private long id;
    private int changeNumber;

    public ChangeAmountDTO(long id, boolean changeNumber) {
        this.id = id;
        this.changeNumber = changeNumber?1:-1;
    }
}
