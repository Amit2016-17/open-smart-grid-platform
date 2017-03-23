/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.smartsocietyservices.osgp.domain.da.valueobjects;

import java.io.Serializable;

public class ProfileFilter extends NodeIdentifier implements Serializable {

    private static final long serialVersionUID = -5169545289993816729L;

    private final boolean all;

    public ProfileFilter(final int id, final String node, final boolean all) {
        super(id, node);
        this.all = all;
    }

    public boolean isAll() {
        return this.all;
    }

}
