package com.bitanalyzer.service.marketSnapshot;


import com.bitanalyzer.domain.marketSnapshot.MarketSnapshot;
import com.bitanalyzer.domain.marketSnapshot.repository.MarketSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MarketSnapshotService {

    private final MarketSnapshotRepository marketSnapshotRepository;

    @Transactional
    public MarketSnapshot save(MarketSnapshot marketSnapshot) {
        return marketSnapshotRepository.save(marketSnapshot);
    }



}
