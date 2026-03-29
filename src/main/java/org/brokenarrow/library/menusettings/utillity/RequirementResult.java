package org.brokenarrow.library.menusettings.utillity;

public interface RequirementResult {

    default void onSuccess() {}

    default void onFailure() {}
}