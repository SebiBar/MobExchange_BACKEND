package mobex;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Currency;

@Table(name = "wallet")
@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
    public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private Currency fromCurrency;
        private Currency toCurrency;
        private double amountFrom;
        private double amountTo;
        private LocalDateTime transactionDate;




}


