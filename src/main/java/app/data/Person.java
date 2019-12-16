package app.data;

public class Person {
        private String count;
        private int uId;
        private String firstName;
        private String secondName;
        private String middleName;

        public Person(){
            count = "0";
            uId = 0;
            firstName = secondName = middleName = "NAN";
        }

        public Person(int uId, String firstName, String secondName, String middleName, String count){
            uId = uId;
            firstName = firstName;
            secondName = secondName;
            middleName = middleName;
            count = count;
        }

        public int getUid(){ return uId;}
        public String getCount(){ return count;}
        public String getFirstName(){ return firstName;}
        public String getSecondName(){ return secondName;}
        public String getMiddleName(){ return middleName;}
        public void setCount(String count) { this.count = count;}
        public void setUid(int uId) { this.uId = uId; }
        public void setFirstName(String name) { firstName = name;}
        public void setSecondName(String name) { secondName = name;}
        public void setMiddleName(String name) { middleName = name;}
}
