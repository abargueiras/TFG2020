/*
 * Copyright FUOC.  All rights reserved.
 * @author Vicenç Font Sagristà, 2012
 */
package jpa;
import java.io.Serializable;

import javax.persistence.*;

/**
 * Enumeració del tipus de proves mèdiques que es poden realitzar.
 *
 */

public enum TestType {
    MagneticResonanceImaging,
    CTScan,
    BloodTest
}