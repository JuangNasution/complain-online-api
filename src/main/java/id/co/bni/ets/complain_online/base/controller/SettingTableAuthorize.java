package id.co.bni.ets.complain_online.base.controller;

import id.co.bni.ets.lib.base.controller.table.TableOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface SettingTableAuthorize<T> extends TableOperation<T> {

    @Override
    @PreAuthorize("hasAnyAuthority('REFUND_ONLINE_USER')")
    Page<T> getTable(String searchTerm, Pageable pageable);
}
