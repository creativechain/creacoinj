/*
 * Copyright 2014 Andreas Schildbach
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.creativecoinj.utils;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.Serializable;
import java.math.BigInteger;

import org.creativecoinj.core.Coin;

import com.google.common.base.Objects;

/**
 * An exchange rate is expressed as a ratio of a {@link Coin} and a {@link Fiat} amount.
 */
public class ExchangeRate implements Serializable {

    public final Coin coin;
    public final Fiat fiat;

    /** Construct exchange rate. This amount of coin is worth that amount of fiat. */
    public ExchangeRate(Coin coin, Fiat fiat) {
        checkArgument(coin.isPositive());
        checkArgument(fiat.isPositive());
        checkArgument(fiat.getCurrencyCode() != null, "currency code required");
        this.coin = coin;
        this.fiat = fiat;
    }

    /** Construct exchange rate. One coin is worth this amount of fiat. */
    public ExchangeRate(Fiat fiat) {
        this(Coin.COIN, fiat);
    }

    /**
     * Convert a coin amount to a fiat amount using this exchange rate.
     * @throws ArithmeticException if the converted fiat amount is too high or too low.
     */
    public Fiat coinToFiat(Coin convertCoin) {
        // Use BigInteger because it's much easier to maintain full precision without overflowing.
        final BigInteger converted = BigInteger.valueOf(convertCoin.getValue()).multiply(BigInteger.valueOf(fiat.getValue()))
                .divide(BigInteger.valueOf(coin.getValue()));
        if (converted.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0
                || converted.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0)
            throw new ArithmeticException("Overflow");
        return Fiat.valueOf(fiat.getCurrencyCode(), converted.longValue());
    }

    /**
     * Convert a fiat amount to a coin amount using this exchange rate.
     * @throws ArithmeticException if the converted coin amount is too high or too low.
     */
    public Coin fiatToCoin(Fiat convertFiat) {
        checkArgument(convertFiat.getCurrencyCode().equals(fiat.getCurrencyCode()), "Currency mismatch: %s vs %s",
                convertFiat.getCurrencyCode(), fiat.getCurrencyCode());
        // Use BigInteger because it's much easier to maintain full precision without overflowing.
        final BigInteger converted = BigInteger.valueOf(convertFiat.getValue()).multiply(BigInteger.valueOf(coin.getValue()))
                .divide(BigInteger.valueOf(fiat.getValue()));
        if (converted.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0
                || converted.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0)
            throw new ArithmeticException("Overflow");
        try {
            return Coin.valueOf(converted.longValue());
        } catch (IllegalArgumentException x) {
            throw new ArithmeticException("Overflow: " + x.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRate other = (ExchangeRate) o;
        return Objects.equal(this.coin, other.coin) && Objects.equal(this.fiat, other.fiat);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(coin, fiat);
    }
}
