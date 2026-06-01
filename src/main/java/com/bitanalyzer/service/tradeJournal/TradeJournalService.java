package com.bitanalyzer.service.tradeJournal;


import com.bitanalyzer.domain.marketSnapshot.MarketSnapshot;
import com.bitanalyzer.domain.marketSnapshot.repository.MarketSnapshotRepository;
import com.bitanalyzer.domain.tradeJournal.TradeJournal;
import com.bitanalyzer.domain.tradeJournal.repository.TradeJournalRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TradeJournalService {

    private final TradeJournalRepository tradeJournalRepository;
    private final MarketSnapshotRepository marketSnapshotRepository;

    @Transactional
    public TradeJournal saveTradeWithSnapshot(TradeJournal tradeJournal, MarketSnapshot marketSnapshot) {
        TradeJournal savedTrade = tradeJournalRepository.save(tradeJournal);

        if (marketSnapshot != null) {
            marketSnapshot.setTradeJournal(savedTrade);
            marketSnapshotRepository.save(marketSnapshot);
        }

        return savedTrade;
    }

    public List<TradeJournal> getRecentTrades(Long userId, int limit) {
        return tradeJournalRepository.findTop30ByUserIdOrderByExecutedAtDesc(userId);
    }

    public TradeJournal findByOrderId(String orderId) {
        return tradeJournalRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 거래입니다. orderId: " + orderId));
    }

    /** 승률 계산  (퍼센트) */
    public double getWinRate(Long userId) {
        Long wins = tradeJournalRepository.countWinTrades(userId);
        Long total = tradeJournalRepository.countTotalTrades(userId);
        return total == 0 ? 0.0 : Math.round((wins * 100.0 / total) * 100) / 100.0; // 소수점 2자리 반올림
    }

    /** 기간별 거래 조회 */
    public List<TradeJournal> getTradesByPeriod(Long userId, LocalDateTime from, LocalDateTime to) {
        return tradeJournalRepository.findByUserIdAndExecutedAtBetween(userId, from, to);
    }

}
