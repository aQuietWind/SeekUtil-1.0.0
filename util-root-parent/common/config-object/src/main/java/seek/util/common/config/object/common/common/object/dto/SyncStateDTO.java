package seek.util.common.config.object.common.common.object.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncStateDTO {
    private long aimId;
    private long ownId;
    private boolean value;
    private int type;
}
