/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 05. 06. 2017
 */
package sk.vilten.vauth.client.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import sk.vilten.vauth.client.models.entity.VauthNfcCode;

/**
 * trieda response
 * @author vt
 * @version 1
 * @since 2017-03-15
 */
@XmlRootElement
public class VAuth_NfcCodeResponse extends BaseResponse implements Serializable {
    private List<VauthNfcCode> nfcCodes;

    public VAuth_NfcCodeResponse() {
        this.nfcCodes = new ArrayList<>();
    }

    public VAuth_NfcCodeResponse(List<VauthNfcCode> nfcCodes) {
        super();
        this.nfcCodes = nfcCodes;
    }

    public List<VauthNfcCode> getNfcCodes() {
        return nfcCodes;
    }

    public void setNfcCodes(List<VauthNfcCode> nfcCodes) {
        this.nfcCodes = nfcCodes;
    }
}
