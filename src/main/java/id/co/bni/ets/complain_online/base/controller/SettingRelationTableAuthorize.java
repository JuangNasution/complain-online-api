package id.co.bni.ets.complain_online.base.controller;

import id.co.bni.ets.lib.base.controller.table.relation.TableRelationOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface SettingRelationTableAuthorize<T, ID> extends TableRelationOperation<T, ID> {

    @Override
    @PreAuthorize("hasAnyAuthority('REFUND_ONLINE_USER')")
    Page<T> getTable(ID relationId, String searchTerm, Pageable pageable);
}
