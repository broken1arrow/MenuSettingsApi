package org.brokenarrow.library.menusettings.utillity;

import org.jetbrains.annotations.NotNull;

/**
 * Handles the outcome of a requirement check by defining actions
 * to execute on success or failure.
 */
public final class RequirementResultHandler {
    private static final Action EMPTY = () -> {};
    private Action onSuccess = EMPTY;
    private Action onFailure = EMPTY;

    /**
     * Sets the action to execute when the requirement check succeeds.
     *
     * @param action the action to execute on success.
     * @return this handler instance for chaining
     */
    public RequirementResultHandler onSuccess(@NotNull final Action action) {
        this.onSuccess = action;
        return this;
    }

    /**
     * Sets the action to execute when the requirement check fails.
     *
     * @param action the action to execute on failure
     * @return this handler instance for chaining
     */
    public RequirementResultHandler onFailure(@NotNull final Action action) {
        this.onFailure = action;

        return this;
    }

    /**
     * Executes the configured success action.
     */
    public void executeSuccess() {
        onSuccess.perform();
    }

    /**
     * Executes the configured failure action.
     */
    public void executeFailure() {
        onFailure.perform();
    }
}