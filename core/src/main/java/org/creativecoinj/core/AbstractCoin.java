package org.creativecoinj.core;

import com.google.common.base.Objects;
import com.google.common.math.LongMath;
import com.google.common.primitives.Longs;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by ander on 20/06/17.
 */
public abstract class AbstractCoin implements Monetary, Comparable<AbstractCoin>, Serializable {

    private static final long serialVersionUID = -7647944301985473437L;
    protected long value;
    protected String currencyCode;
    private int exponent;

    public AbstractCoin(long value, String currencyCode, int exponent) {
        this.value = value;
        this.currencyCode = currencyCode;
        this.exponent = exponent;
    }

    @Override
    public long getValue() {
        return value;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    @Override
    public int smallestUnitExponent() {
        return exponent;
    }

    public boolean isPositive() {
        return signum() == 1;
    }

    public boolean isNegative() {
        return signum() == -1;
    }

    public boolean isZero() {
        return signum() == 0;
    }

    public boolean isGreaterThan(AbstractCoin other) {
        return compareTo(other) > 0;
    }

    public boolean isLessThan(AbstractCoin other) {
        return compareTo(other) < 0;
    }

    public <C extends AbstractCoin> C add(C value) {
        checkArgument(value.currencyCode.equals(currencyCode));
        this.value = LongMath.checkedAdd(this.value, value.getValue());
        return (C) this;
    }

    public <C extends AbstractCoin> C subtract(C value) {
        checkArgument(value.currencyCode.equals(currencyCode));
        this.value = LongMath.checkedSubtract(this.value, value.getValue());
        return (C) this;
    }

    public <C extends AbstractCoin> C multiply(long factor) {
        this.value = LongMath.checkedMultiply(this.value, factor);
        return (C) this;
    }

    public <C extends AbstractCoin> C divide(long divisor) {
        this.value = this.value / divisor;
        return (C) this;
    }

    public <C extends AbstractCoin> long divide(C divisor) {
        checkArgument(divisor.currencyCode.equals(currencyCode));
        return this.value / divisor.getValue();
    }

    @Override
    public int signum() {
        if (this.value == 0)
            return 0;
        return this.value < 0 ? -1 : 1;
    }


    @Override
    public String toString() {
        return Long.toString(this.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value, currencyCode);
    }

    @Override
    public int compareTo(AbstractCoin other) {
        if (!this.currencyCode.equals(other.currencyCode))
            return this.currencyCode.compareTo(other.currencyCode);
        return Longs.compare(this.value, other.value);
    }
}
