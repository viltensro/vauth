/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 14. 12. 2017
 */
package sk.vilten.vauth.web.exception;

/**
 * Not found exception
 * @author vilten
 */
public class NotFoundException extends Exception {

    public NotFoundException(String message) {
        super(message);
    }
    
}
