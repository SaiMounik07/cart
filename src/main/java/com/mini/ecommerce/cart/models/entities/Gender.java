package com.mini.ecommerce.cart.models.entities;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum Gender {
    @JsonEnumDefaultValue
    MALE,FEMALE,OTHERS;
}
