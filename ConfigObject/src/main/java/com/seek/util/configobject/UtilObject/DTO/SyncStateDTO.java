package com.seek.util.configobject.UtilObject.DTO;

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
