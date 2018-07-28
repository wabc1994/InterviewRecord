package syn;

public class Innerclass {
    private String username;
    private String password;
    class Privateclass {
        private String age;
        private String address;

        public void setAge(String age) {
            this.age = age;
        }

        public String getAge() {
            return age;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
        public void printPublicProperty(){
            System.out.println(username+" " + password);
        }
    }
    public String getUsername(){
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static void main(String[] args) {
        Innerclass innerclass = new Innerclass();
        innerclass.setUsername("liu");
        innerclass.setPassword("1111");
        System.out.println(innerclass.getUsername()+" "+innerclass.getPassword());
        //内部类的初始化机制
        Privateclass privateclass = innerclass.new Privateclass();
        privateclass.setAge("agevalue");
        privateclass.setAddress("addressvalue");
        System.out.println(privateclass.getAge()+ "  " +privateclass.getAddress());
    }
}
