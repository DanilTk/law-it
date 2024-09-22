Encode:

mvn jasypt:encrypt-value -Djasypt.encryptor.password=MySecretKey -Djasypt.plugin.value=myPassword

Decode:

mvn jasypt:decrypt-value -Djasypt.encryptor.password=MySecretKey -Djasypt.plugin.value=ENC(XsGP8uozNnjLuvcAojDQqfiz/8kK1Mhv)