package logic;

/**
 * SpendEX — BudgetCalculable Interface
 * Abstraction for calculating impact on SpendEX budget
 */
public interface BudgetCalculable {
    /**
     * Calculate the financial impact on the daily budget
     * @return impact amount in AED
     */
    double calculateImpact();
}
