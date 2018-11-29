/*
 * Copyright (C) 2017 vt
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sk.vilten.vauth.web.responses;

import sk.vilten.vauth.data.responses.BaseResponse;
import sk.vilten.vauth.web.entity.Token;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * token response for oauth2
 * @author vt
 * @version 1
 * @since 2017-03-19
 */
/**
 * @apiDefine tokenResponseSuccess
 * @apiSuccess (Success-Response) {String} type = tokenResponse
 * @apiSuccess (Success-Response) {Boolean} success = TRUE
 * @apiSuccess (Success-Response) {token} token token entity
 * @apiSuccessExample {json} Success-Response:
 * HTTP/1.1 200 OK
 * {
 * "type": "tokenResponse",
 * "success": true,
 * "token":
 *      {
 *          "token": "c8210a4a0d10dbb1f3160cb61b188923",
 *          "expirationToken": "4235e9b95904c39c7f06a75ed0de700c",
 *          "created": "2017-01-01 12:39:00.000",
 *          "expire_in": "2017-01-01 12:39:00.000",
 *          "roles":
 *              [
 *                  "vauth_admin",
 *                  "vauth_user_write"
 *              ],
 *          "user_id" : 12,
 *          "user_external_id": "manager",
 *          "ip": "192.168.1.12",
 *          "userAgent": "VauthClient v1.1.0"
 *      }
 * }
 */
@XmlRootElement
public class TokenResponse extends BaseResponse implements Serializable {
    private Token token;

    public TokenResponse() {
        super();
    }

    public TokenResponse(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
