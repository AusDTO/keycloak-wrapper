package com.dto.identity;

public class KeyCloakServer
{

    public static void main( String[] args )
    {
        KeyCloakBootStrapper bootStrapper= new KeyCloakBootStrapper();

        try{
            KeyCloakServerConfig config = new KeyCloakServerConfig();
            bootStrapper.start(config);

        }catch (Throwable t){
            t.printStackTrace();
        }
    }
}
