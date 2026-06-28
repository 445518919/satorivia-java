package com.satoriviacafe.cafe_web.constants;

/**
 *
 *
 * @author shy
 * @since 2025年09月02日
 */
public class Constants {
    public static final String APP_ID = "2088070666439058";
    /**
     * 线上域名
     */
    public static final String HOST = "https://tschain.cloud.alipay.com/";
    public static final String RAW_APP_PRIVATE_KEY = """
             -----BEGIN PRIVATE KEY-----
            MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDWOajpy9DiXN1i
            H+0gPzu8/uNvKgtJsajk2j+aZvMNdb56zwlRreAYdbr+EaAWcft6iD6ZVno/IsOT
            3WTeBFRTAj/PVQBPxlVF7H0bYPsmXE4lKUOihQsPp4aau48WVpg3JpFwJ3FkEo36
            UZk8ZefrTmk/CdH8AaT9QVeUCiyAf42ab8AP4a0k11/OJ7YgkfAfJ8q9ZRqZzvEB
            1R8hgehK9VRMAyiEvGWv5nDSKQk5jy6KghFR04t8CiH1n16pwyMG+UrwYCZ6JDHn
            j2Np2h4KU9oJ+4HeylaNynOHpw2onexEnV2O4taVfiGIdUW7LlK4sI2oleFjCeGD
            Z3dtsLZBAgMBAAECggEAKUZUwC8P4P1JfkVud58XUHO/uERehmVfiDBwvrwFKlu8
            RocrYXxhx/SvAZK9yHVPoa0senjJ9u4oxo2KiKhG4vN0/ep/s98fwOgutp655Qs1
            1z0bFe4SUoLRkXB/wisPCtk4fmZrF3/nsQoVvyhXReQGrSmkNqzOcCktyY0oQ911
            PqSop6pk+lUzLUWhgejG6IxTejXhZkhaE7Tco0WiZLGeFnL9uytjfI+GsEJAWgdq
            2a/odoK6wGhVD3Td70eJeut/7xvHdFuHCP03meAKW9wKimZqr/7sFTOoia8Dntge
            KsJtkO9ffbwjeFI5WTFzuHjgxc2nzr4STS6JKwoSzQKBgQD16Q/g+wr7U0CCZTYV
            +vVzIx6aj3Onz6iaoH0dHRTyRD/lzCD1JfZFd2XRszrYDkbQTMtPrZx90BHCHeou
            KNdMPZY08OxaMrp7bL526r41szrz5AAXBw1SZN29ULu4F3FrRc+d1j+ICwbzGxjm
            /f0m+DpCqPdE9krJB49D9FRSfwKBgQDfA8pTAGv1ihl645Cjn4NfVloUvONTR9Aq
            YmtnM8KLYNOVYj3LhgBw3yzDYAgR0dvS0IXfal6P6MUxeu9y9XeNWEyLT66z7VX6
            bYLgjaHqATVbChX+rCg5QUdsSd1CBpszJUWPzm8IIIVjqE23+g8H/W8my6s6t1t3
            3i1DhPkXPwKBgQCwJw1q2ioZsp9JSPsfb7AXlBa+HPh6QzTB3ykAi+6PRPiIwlrx
            gQoS5N6ZAsyqLD/pwrj5EKJDNaMi7I7uFe9z+8H3loWYbzJKa7FcOfebPBKE1tSB
            qlmOphZ5ILX3v0NN1qpMpEbKSyvlRYnnHhYTrkj9q0/FpLAZpUKpKQKmPQKBgEGb
            i9n/NcN2bSiJ6fM46h0R+k4/g2Dsr4DIk3A4+fKm+Zfo3uUQ9vA9t52Ckt0rL9BF
            KB0h3c/YWqrDz82yCQm8AEi7mFm2aKTTsrz87bU5xciSnwXc/7hYS/q/gCf8aYwS
            WvSDdoadiqBW0wZcui9Pj3OKV0Vey7nn0skaRW1hAoGBANBQN9lBqNk7XBVlaG1W
            mojCWBJCbxxlEkQ5SmakAM/0QSEtVj0BDwP9oR5BtnLFMKIW/PKiqM/JUrsah6B9
            WL0pOaFy+O1rVN99BnmRITxoOlY9Z1M6E3U+ibnKz6hYBBDH0m8UDUl5NWMSG+EC
            YOeqVTO7QH9iCT81zmIlp/UP
            -----END PRIVATE KEY-----
            """;
    public static String APP_PRIVATE_KEY = RAW_APP_PRIVATE_KEY.replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replaceAll("\\s+", "");
}
