/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.smartsocietyservices.osgp.domain.da.valueobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetDataResponse implements Serializable {

    private static final long serialVersionUID = 7345936024521675762L;

    private final List<GetDataSystemIdentifier> getDataSystemIdentifiers;

    public GetDataResponse(final List<GetDataSystemIdentifier> getDataSystemIdentifiers) {
        this.getDataSystemIdentifiers = new ArrayList<>(getDataSystemIdentifiers);
    }

    public List<GetDataSystemIdentifier> getGetDataSystemIdentifiers() {
        return new ArrayList<>(this.getDataSystemIdentifiers);
    }

}
