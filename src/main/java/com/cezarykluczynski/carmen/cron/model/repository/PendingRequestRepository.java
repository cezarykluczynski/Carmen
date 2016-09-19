package com.cezarykluczynski.carmen.cron.model.repository;

import com.cezarykluczynski.carmen.cron.model.entity.PendingRequest;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PendingRequestRepository extends JpaRepository<PendingRequest, Long>, PendingRequestRepositoryCustom {

    List<PendingRequest> findAllByUser(User user);

    PendingRequest findFirstByOrderByPriorityDesc();

}
