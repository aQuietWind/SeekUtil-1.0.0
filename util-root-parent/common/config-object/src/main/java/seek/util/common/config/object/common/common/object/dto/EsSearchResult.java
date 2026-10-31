package seek.util.common.config.object.common.common.object.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsSearchResult<T> {
    private List<T> results=new ArrayList<>();
    private List<Object> lastSortValues;
}
