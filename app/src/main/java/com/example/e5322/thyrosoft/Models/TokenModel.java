package com.example.e5322.thyrosoft.Models;

public class TokenModel {

    /**
     * RespId : RES0000
     * ResponseMessage : Token Generated Successfully
     * Id : 431
     * UserId : U000431
     * UserCode : SELF_DOC
     * Name : SELF REGISTERED DOCTOR
     * Address : null
     * Pincode : null
     * Mobile : null
     * Email : null
     * Role : 1
     * LoginType : New User
     * Company : NHL
     * API_Key : null
     * access_token : 1HfOKkSjsea6FiP3yhkeshyHRAeYt6Yq4zwPi0JEBf6UDAxmCuIxhVrV3vXh-3EiUOulrD_iTjBeHn7tIpGROotghA8Gql9aG78nQZKfNcZEzelEFiUBcMwTtpYpe7RXQH7Jd00evnX50fy37M6TeeUfjZqt3CE5Zs8w5JD4UansL73PppCVcZAaV97I2nlsnjSyM2Nbe8d72qEQ9LXodDIk8Pea8MHO-FVVzVxz5K3ZHiL66xsP4smBOs0bLJKd1qLXvUsvrNzHU8Rmg8mMYRq8Zor03haxnUgMO-ypQ4IygpSAYjhJbaVXEYgyVt7iwl7XfqHFr3-rUFEsX8VCTzNZFXZLftRFD6DbhWHovoMyPZMXyGBqF6JLuS2IQQZaLvf1oaqrRj44cAtbtXUwhRtxsGGvsmbuYUShQyoSKhdzn_6mmY4IHK1Ib3FfqFI9q4wPSf94EX786zx8UvOycUa235ltBMllXVRlmcwwDFs
     * token_type : bearer
     * expires_in : 2591999
     * _issued : null
     * _expires : null
     * error : null
     * error_description : null
     * Parent : null
     * Center : {"CenterId":"CN00000013","Name":"NUECLEAR HEALTHCARE LIMITED, NAVI MUMBAI","Addres":null,"Pincode":null,"Mobile":null,"Email":null,"Type":"CPL","ContactPerson":null,"ContactPersonNo":null}
     */

    private String RespId;
    private String ResponseMessage;
    private int Id;
    private String UserId;
    private String UserCode;
    private String Name;
    private Object Address;
    private Object Pincode;
    private Object Mobile;
    private Object Email;
    private String Role;
    private String LoginType;
    private String Company;
    private Object API_Key;
    private String access_token;
    private String token_type;
    private String expires_in;
    private Object _issued;
    private Object _expires;
    private Object error;
    private Object error_description;
    private Object Parent;
    private CenterBean Center;

    public String getRespId() {
        return RespId;
    }

    public void setRespId(String RespId) {
        this.RespId = RespId;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String ResponseMessage) {
        this.ResponseMessage = ResponseMessage;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String UserCode) {
        this.UserCode = UserCode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public Object getAddress() {
        return Address;
    }

    public void setAddress(Object Address) {
        this.Address = Address;
    }

    public Object getPincode() {
        return Pincode;
    }

    public void setPincode(Object Pincode) {
        this.Pincode = Pincode;
    }

    public Object getMobile() {
        return Mobile;
    }

    public void setMobile(Object Mobile) {
        this.Mobile = Mobile;
    }

    public Object getEmail() {
        return Email;
    }

    public void setEmail(Object Email) {
        this.Email = Email;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String Role) {
        this.Role = Role;
    }

    public String getLoginType() {
        return LoginType;
    }

    public void setLoginType(String LoginType) {
        this.LoginType = LoginType;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String Company) {
        this.Company = Company;
    }

    public Object getAPI_Key() {
        return API_Key;
    }

    public void setAPI_Key(Object API_Key) {
        this.API_Key = API_Key;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public Object get_issued() {
        return _issued;
    }

    public void set_issued(Object _issued) {
        this._issued = _issued;
    }

    public Object get_expires() {
        return _expires;
    }

    public void set_expires(Object _expires) {
        this._expires = _expires;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public Object getError_description() {
        return error_description;
    }

    public void setError_description(Object error_description) {
        this.error_description = error_description;
    }

    public Object getParent() {
        return Parent;
    }

    public void setParent(Object Parent) {
        this.Parent = Parent;
    }

    public CenterBean getCenter() {
        return Center;
    }

    public void setCenter(CenterBean Center) {
        this.Center = Center;
    }

    public static class CenterBean {
        /**
         * CenterId : CN00000013
         * Name : NUECLEAR HEALTHCARE LIMITED, NAVI MUMBAI
         * Addres : null
         * Pincode : null
         * Mobile : null
         * Email : null
         * Type : CPL
         * ContactPerson : null
         * ContactPersonNo : null
         */

        private String CenterId;
        private String Name;
        private Object Addres;
        private Object Pincode;
        private Object Mobile;
        private Object Email;
        private String Type;
        private Object ContactPerson;
        private Object ContactPersonNo;

        public String getCenterId() {
            return CenterId;
        }

        public void setCenterId(String CenterId) {
            this.CenterId = CenterId;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public Object getAddres() {
            return Addres;
        }

        public void setAddres(Object Addres) {
            this.Addres = Addres;
        }

        public Object getPincode() {
            return Pincode;
        }

        public void setPincode(Object Pincode) {
            this.Pincode = Pincode;
        }

        public Object getMobile() {
            return Mobile;
        }

        public void setMobile(Object Mobile) {
            this.Mobile = Mobile;
        }

        public Object getEmail() {
            return Email;
        }

        public void setEmail(Object Email) {
            this.Email = Email;
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
        }

        public Object getContactPerson() {
            return ContactPerson;
        }

        public void setContactPerson(Object ContactPerson) {
            this.ContactPerson = ContactPerson;
        }

        public Object getContactPersonNo() {
            return ContactPersonNo;
        }

        public void setContactPersonNo(Object ContactPersonNo) {
            this.ContactPersonNo = ContactPersonNo;
        }
    }
}
