
## 1- property --> id, usurname, email, password, activationCode(String),
### ERole(USER; ADMIN), EStatus(PENDING, ACTIVE, INACTIVE, DELETED, BANNED)
### Bu sınıfın repository, service, controller' ını oluşturunuz.
### Utility katmanını oluşturunuz, Crud metotları için gerekli repo paternini kurunuz.

##  2- Register için bir requestDto ve responseDto oluşturunuz.
###  Mapper katmanından yararlanarak AuthService' de register metodunu yazınız ve test ediniz.
###  requestDto --> username,email, password
###  responseDto --> id, username, activationCode
###  Register metodu yazılacak

## 3- (boolean)activateStatus metodunu yazınız. Bu metot için bir dto oluşturunuz. Dto --> id, activationCode
### activateStatus metodu login metodundan önce çalışacaktır. Register olduğumuzda gelen kod burada girilerek kullanıcının durumu
### ACTIVE olarak değiştirilir. Daha sonra login işlemi yapılabilir.

## 4- (boolean)login metodu yazınız. Login metodu için bir dto oluşturunuz. Dto --> username, password
### Kullanıcının status' ü ACTIVE yapılmadıysa hata dönmelidir.
### Exception katmanını auth-service' e dahil ediniz.


## 5- OpenFeign için gerekli işlemleri yapınız. Bağımlılığını ekleyiniz, bir dto(NewCreateUserRegisterDto) oluşturarak
## auth-service' de yapılan register işleminin user-service' de çalışmasını sağlayınız. OpenFeign için 'manager' katmanını oluşturunuz.

## Auth-Service'deki activateStatus metodu kullanıldığında register olan kullanıcının durumu ACTIVE olarak değişmektedir.
## Ancak bu değişiklik User-Profile service'e aktarılan kullanıcının durumunda bir değişiklik yaratmamaktadır.
## Bu durumu gidermek için activateStatus metodu feign client ile user-profile service' e aktarılmalıdır. 
## Bunun için gerekli işlemleri yapınız.

### UserManager' da yazacağınız activateStatus metodu yalnızca authId parametresi almalıdır.

## 6- UserProfile' daki eksik kalan bilgiler veya username, email gibi AuthService' den gelen bilgiler update edilebilmelidir.
### Bu işlem için UserProfileUpdateDto adında bir dto oluşturarak gerekli işlemleri yapınız.

### Bu bilgiler içerisinden username ve email bilgilerinin OpenFeign kullanılarak AuthService' e gönderilmesi gerekmektedir.
### Bunun için de bir dto' ya ihtiyaç duyulabilir. 'UpdateEmailOrUsernameRequestDto' oluşturup bu dto üzerinden veriler
### AuthService' e gönderilmelidir.

## Login metodu kullanıcıya bir token dönmelidir. Bunun için Login metodunu refactor ediniz.
### Burada değiştireceğiniz kısım metodun dönüş tipidir.
### Exception katmanına token' ın üretilemediğine dair bir enum yazınız ve bunu Login metodunda kullanınız.(TOKEN_NOT_CREATED)

## Login işleminden alınan token bilgisi, user-profile-service' de update işlemi için kullanılmalıdır.
## Bu işlem için update metoduna gönderilen dto' ya bir 'token' property' si eklenmelidir.
## user-profile-service' de JwtTokenProvider oluşturulmalıdır. Daha sonra update metodu içerisinde kullanılabilir.

# 7- Config server için gerekli işlemleri yapınız.
## config-server'ın application.yml' ını yazınız.
## config-repo adında bir paket oluşturarak içerisine auth-service ve userprofile-service' in ayarlarını giriniz
## userprofile ve auth service' de içi boşalan .yml dosyalarını config-server üzerinden çalıştıracak ayarları yazınız

# 8- Projenize Zipkin' in bağımlılıklarını ekleyiniz.
## Zipkin için bir imaj oluşturunuz ve gerekli ayarları projenizde yapınız.

# 9- UserService'de bir metot(findByUsername()) yazmalalıyız. Bu metot username'e göre veritabnından veriyi getirmelidir.
## Büyük küçük harf duyarlılığını ortadan kaldırmalıyız. Metot için gerekli hata kontrollerini yapınız.
## Metottan gelen veriyi cacheleyiniz.

# 10- Veritabanındaki bütün kullanıcıları döndüren metodu cache'leyiniz. Veritabanında bir değişiklik olduğunda bu cache silinsin ve
## yeni kullanıcılar veri setinde gösterilebilsin. Veriler tekrar cache'lenmiş olsun.

# 11- findByRole metodu için kayıt oluşturulduğunda veya kullanıcı bilgileri güncellendiğinde cache'in silinmesi gereklidir.
## Bunun için gerekli işlemleri yapınız.
## --> findByRole için put işlemi userProfileService'de yapılmamaktadır. Bunun işlemi AuthService'de deneyiniz.

# 12- Mail-service için rabbitmq konfigürasyonunu yazınız, rabbitmq ile gelen veriyi tüketmek için consumer ve model katlmanlarını oluşturunuz.
# MailService sınıfı aracılığıyla bu bilgileri JavaMailSender sınıfına veriniz. Ve kodlarınızı test ediniz.

# 13- utility katmanında GetAllData adında bir sınıf oluşturunuz. Bu sınıfın içerisinde initData() adında bir metot olacak.
## Bu metot manager katmanı aracılığıyla userProfileService'den bütün verileri çekecek ve elasticsearch'ün in-memory'sine kayıt edecek
## Bu metot program çalıştığında bir kere çalışacak ve bir daha çalışmayacak.
## Bu verilerin elasticsearch'e kayıt olduğunu görüntülemek için burada bir findAll metodu da olmalıdır.

# 14- AuthService'de yapılan kayıt işlemi(registerWithRabbitMq) verileri user-profile service' e gelmektedir.
## Buraya gelen veriler elastic-service'e gönderilmeli ve burada in-memory'ye kayıt edilmelidir.
## Bunun için user-profile service'deki config, rabbitmq, manager ve service katmanında gerekli işlemleri yapınız.
## user-profile 'producer', elastic-service 'consumer' olmalıdır.

# 15- ElasticSearch' deki verilerin pageable edilmesi gerekmektedir. Bunun için gerekli işlemleri yapınız.
## Kullancağınız metot findAll metodudur.

# 16- UserProfile-service'de, ChangePassword işleminin yapılması
## Bunun için bir dto oluşturulmalıdır. Dto --> token, oldPassword, newPassword
## oldPassword kontrol edilmelidir ve buna göre newPassword gelen veri ile set edilmelidir.
## Bu işlemleri yapan metodun adı 'passwordChange()', boolean
## Buradaki yapılan değişiklik auth-service'e gönderilmelidir.

# 17- Auth-service'de yapılan forgotPassword işleminde kullanıcının şifresi, üretilen yeni bir şifre ile değiştirilmektedir.
## Bu değişikliğini userprofile-service'e de aktarılması için OpenFeign yöntemi ile gerekli işlemleri yapınız.

# 18- Post-service'derki creatNewPost metodu için bir rabbitmq aksiyonu yazınız. İstek userprofile-service'e gönderilecek ve gelen veri
## post-service'de tüketilecektir.

# 19- Post-service'deki Post ve Like servislerde CRUD metotlarının yazılması gerekmektedir. Bunun için gerekli işlemleri yapınız.
## Ayrıca Dislik service'de de dislikePost işleminin yapılması gerekmektedir.

# SECURITY
##  Temelde iki çeşittir.
### 1-Token Tabanlı Kimlik Doğrulama
### 2-Session(Oturum) Tabanlı Kimlik Doğrulama

#### 1.1. Kullanıcının giriş yapmasıyla beraber kullanıcının id ve role bilgilerine göre özel bir token üretilir.
#### Bu tokendan role bilgisini alınarak doğrulama işlemi yapılır. Kullanıcının rolü sınırlandırılabilir ve buna göre kullanıcı erişimi kısıtlayabilir.

#### 1.2. Kullanıcı sisteme girer ve arka tarafta kullanıcıya ait bir oturum bilgisi tutulur. Kullanıcı bu bilgi sayesinde belirli bir süre sisteme username ve password girmesine gerek kalmaz.
#### Temel olarak WebSecurityConfigurerAdapter, HttpSessionListener, AuthenticationSuccessHandler sınıflarından oluşan bir yapıdır.
#### Session tabanlı kimlik doğrulama da token üretilmez. Bunun yerine sunucu taraflı bir session üretilir.
#### Bu oturum bilgisinin içerisinde kullanıcının id, username, email, password, phone gibi unique bilgileri vardır.