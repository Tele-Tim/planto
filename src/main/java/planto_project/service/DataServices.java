package planto_project.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import planto_project.dto.SortingDto;
import planto_project.dto.filters_dto.FilterDto;
import planto_project.model.Filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DataServices{
    public default Pageable getPageable(SortingDto sortingDto) {
        Sort.Direction direction = sortingDto.getDirection() > 0 ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(sortingDto.getPage(), sortingDto.getSize(), Sort.by(direction, sortingDto.getField().toLowerCase()));
    }

    public default Query getQueryWithCriteria(Map<String, Filter<?>> mapFilters, SortingDto sortingDto) {

        List<FilterDto> filtersDto = sortingDto.getCriteria();
        List<Criteria> criteria = new ArrayList<>();
        for (FilterDto filterDto : filtersDto) {
            String field = filterDto.getField();

            Filter<?> filter = mapFilters.get(field);

            // in list
            if (filterDto.getType() == 3) {
                filter = new Filter<>(filterDto.getField(),
                        filterDto.getType(),
                        filterDto.getValueList());
                // in range
            } else if (filterDto.getType() == 2) {
                filter = new Filter<>(filterDto.getField(),
                        filterDto.getType(),
                        filterDto.getValueFrom(),
                        filterDto.getValueTo());
            } else {
                filter = new Filter<>(filterDto.getField(),
                        filterDto.getType(),
                        (String) filterDto.getValue());
            }

            Criteria queryCriteria = filter.getCriteria();
            if (queryCriteria != null) {
                criteria.add(queryCriteria);
            }
        }

        Query query = new Query();
        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria));
        }

        return query;
    }
}
