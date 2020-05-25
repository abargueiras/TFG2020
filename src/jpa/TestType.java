/*
 * Copyright FUOC.  All rights reserved.
 * @author Vicen� Font Sagrist�, 2012
 */
package jpa;
import java.io.Serializable;

import javax.persistence.*;

/**
 * Enumeraci� del tipus de proves m�diques que es poden realitzar.
 *
 */

public enum TestType {
    MagneticResonanceImaging,
    CTScan,
    BloodTest
}