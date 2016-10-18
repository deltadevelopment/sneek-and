package no.twomonkeys.sneek.app.shared;

import no.twomonkeys.sneek.app.shared.models.ErrorModel;

/**
 * 26/09/16 by chridal
 * Copyright 2MONKEYS AS
 */

public interface NetworkCallback {
    void exec(ErrorModel errorModel);
}
