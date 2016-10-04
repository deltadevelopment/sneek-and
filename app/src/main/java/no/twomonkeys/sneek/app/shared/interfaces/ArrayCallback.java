package no.twomonkeys.sneek.app.shared.interfaces;

import java.util.ArrayList;

import no.twomonkeys.sneek.app.shared.models.ErrorModel;

/**
 * Created by Christian Dalsvaag on 04/10/16
 * Copyright 2MONKEYS AS
 */

public interface ArrayCallback {

   void exec(ArrayList arrayList, ErrorModel errorModel);

}
