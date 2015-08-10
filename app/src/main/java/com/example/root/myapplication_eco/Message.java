package com.example.root.myapplication_eco;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by Antonio on 6/27/2015.
 */
public class Message {
    private boolean lRun = false;
    private String serverMessage;
    public static final String SERVERIP = "10.0.0.12";
    public static final int SERVERPORT = 5551;

    PrintWriter out;
    BufferedReader in;


    public static class AndroidToServer implements Serializable {

        public enum dataToBeRequested {
            NULL,TASKDATA, TASKIMAGEDATA, EMPLOYEEDATA,
            TASKPICTURES, MAINTENANCEPICTURES, PRIORITY1, PRIORITY2, PRIORITY3
        }

        private dataToBeRequested req = dataToBeRequested.NULL;//default value

        public dataToBeRequested getRequest() {
            return req;
        }

        public void setRequest(dataToBeRequested dtbr) {
            this.req = dtbr;
        }

        public static class LogInMessage implements Serializable {
            private String ID;
            private String username;
            private String password;
            public LogInMessage(String u, String p) {
                username = u;
                password = p;

            }
            public String getUsername() {
                return username;
            }
            public void setUsername(String username){
                this.username= username;}

            public String getPassword() {
                return password;
            }
            public void setPassword(String password){
                this.password= password;}

            public String getID() {
                return ID;
            }
            public void setID(String ID){
                this.ID= ID;}
        }
        public static class logOut implements Serializable {
            static final long serialVersionUID = -5;
            private String Id;

            public logOut (String Id) {
                this.Id= Id;
            }

            public void setId(String Id) {
                this.Id=Id;
            }
            public String getId() {
                return Id;
            }


        }//LOGOUT

        public static class RegisterEmployee implements Serializable {
            static final long serialVersionUID = -52888311;
            private String last_name="", first_name="",  email="",phone="",
                    address="", city="", zip="",state="",birthdate="",
                    hiredate="", leavedate="",ssn="",active="",position="",User="",Password="";

            public RegisterEmployee(String lastName, String firstName, String email, String phone, String address, String city, String zip,
                                    String state,String birthdate, String hiredate, String leavedate, String ssn,String active,String position,
                                    String User, String Password) {
                this.last_name=lastName;
                this.first_name=firstName;
                this.email=email;
                this.phone= phone;
                this.address=address;
                this.city=city;
                this.zip=zip;
                this.state=state;
                this.birthdate=birthdate;
                this.hiredate=hiredate;
                this.leavedate=leavedate;
                this.ssn=ssn;
                this.active=active;
                this.position=position;
                this.User= User;
                this.Password= Password;

            }

            public void setLastName(String lastname) {
                this.last_name=lastname;
            }

            public String getLastName() {
                return last_name;
            }


            public void setFirstName(String firstname) {
                this.first_name=firstname;
            }

            public String getFirstName() {
                return first_name;
            }
            public void setEmail(String email){
                this.email= email;}
            public String getEmail (){
                return email;}
            public void setPhone(String phone) {
                this.phone=phone;
            }

            public String getPhone() {
                return phone;
            }
            public void setAddress(String address) {
                this.address=address;
            }

            public String getAddress() {
                return address;
            }
            public void setCity(String city) {
                this.city=city;
            }

            public String getCity() {
                return city;
            }
            public void setZip(String zip) {
                this.zip=zip;
            }

            public String getZip() {
                return zip;
            }
            public void setState(String state) {
                this.state=state;
            }

            public String getState() {
                return state;
            }
            public void setBirthdate(String birthdate) {
                this.birthdate=birthdate;
            }

            public String getBirthdate() {
                return birthdate;
            }
            public void setHiredate(String hiredate) {
                this.hiredate=hiredate;
            }

            public String getHiredate() {
                return hiredate;
            }
            public void setLeavedate(String leavedate) {
                this.leavedate=leavedate;
            }

            public String getLeavedate() {
                return leavedate;
            }
            public void setSsn(String ssn) {
                this.ssn=ssn;
            }

            public String getSsn() {
                return ssn;
            }
            public void setActive (String active) {
                this.active=active;
            }

            public String getActive() {
                return active;
            }
            public void setPosition(String position) {
                this.position=position;
            }

            public String getPosition() {
                return position;
            }
            public void setUser(String User) {
                this.User=User;
            }

            public String getUser() {
                return User;
            } public void setPassword(String Password) {
                this.Password=Password;
            }

            public String getPassword() {
                return Password;
            }

        }//REGISTEREMPLOYEE

        public static class CreateTaskTask implements Serializable {
            static final long serialVersionUID = -52888312;
            private String tTitle;
            private String tDesc;
            private String tSpin;

            public CreateTaskTask(String tTitle, String tDesc, String tSpin) {
                this.tTitle=tTitle;
                this.tDesc=tDesc;
                this.tSpin=tSpin;
            }

            public void settTitle(String tTitle) {
                this.tTitle=tTitle;
            }
            public String gettTitle() {
                return tTitle;
            }

            public void settDesc(String tDesc) {
                this.tDesc=tDesc;
            }

            public String gettDesc() {
                return tDesc;
            }

            public void settSpin(String tSpin) {
                this.tSpin=tSpin;
            }

            public String gettSpin() {
                return tSpin;
            }
        }//CREATETASKTASK


        public static class PictureMessage implements Serializable {
            static final long serialVersionUID = -52888313;
            private byte[] picture;
            private String id;

            PictureMessage(byte[] picture, String id) {
                this.picture = picture;
                this.id = id;
            }

            public byte[] getPicture() {
                return picture;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setPicture(byte[] picture) {
                this.picture = picture;
            }
        }//PICTUREMESSAGE
    }//ANDROIDTOSERVER CLASS
    public static class ServerToAndroid {
        public static class TaskDataClass implements Serializable {
            private int task_id=-1;
            private String title = "";
            private String description = "";
            private String spin="";



            public void SetTaskID(int taskid) {
                this.task_id=taskid;
            }

            public int GetTaskID() {
                return this.task_id;
            }

            public void setTitle(String t) {
                this.title=t;
            }

            public String getTitle() {
                return this.title;
            }
            public void setDescription(String d){
                this.description=d;
            }
            public String getDescription() {
                return this.description;
            }
            public void setSpin(String s){
                this.spin=s;
            }
            public String getSpin() {
                return this.spin;
            }

        }
        public static class returnTaskData implements Serializable {
            private ArrayList taskdata = new ArrayList();

            public void addTask(TaskDataClass tdc) {
                taskdata.add(tdc);
            }
            public TaskDataClass getNextTask() {
                if(taskdata.isEmpty())
                    return null;
                else
                    return (TaskDataClass)taskdata.remove(0);

            }
        }
        public static class p2TaskDataClass implements Serializable {
            private int task_id=-1;
            private String title = "";
            private String description = "";
            private String spin="";



            public void SetTaskID(int taskid) {
                this.task_id=taskid;
            }

            public int GetTaskID() {
                return this.task_id;
            }

            public void setTitle(String t) {
                this.title=t;
            }

            public String getTitle() {
                return this.title;
            }
            public void setDescription(String d){
                this.description=d;
            }
            public String getDescription() {
                return this.description;
            }
            public void setSpin(String s){
                this.spin=s;
            }
            public String getSpin() {
                return this.spin;
            }

        }
        public static class p2returnTaskData implements Serializable {
            private ArrayList p2taskdata = new ArrayList();

            public void addTask(p2TaskDataClass p2tdc) {
                p2taskdata.add(p2tdc);
            }
            public p2TaskDataClass getNextTask() {
                if(p2taskdata.isEmpty())
                    return null;
                else
                    return (p2TaskDataClass)p2taskdata.remove(0);

            }
        }
        public static class p3TaskDataClass implements Serializable {
            private int task_id=-1;
            private String title = "";
            private String description = "";
            private String spin="";



            public void SetTaskID(int taskid) {
                this.task_id=taskid;
            }

            public int GetTaskID() {
                return this.task_id;
            }

            public void setTitle(String t) {
                this.title=t;
            }

            public String getTitle() {
                return this.title;
            }
            public void setDescription(String d){
                this.description=d;
            }
            public String getDescription() {
                return this.description;
            }
            public void setSpin(String s){
                this.spin=s;
            }
            public String getSpin() {
                return this.spin;
            }

        }
        public static class p3returnTaskData implements Serializable {
            private ArrayList p3taskdata = new ArrayList();

            public void addTask(p3TaskDataClass p3tdc) {
                p3taskdata.add(p3tdc);
            }
            public p3TaskDataClass getNextTask() {
                if(p3taskdata.isEmpty())
                    return null;
                else
                    return (p3TaskDataClass)p3taskdata.remove(0);

            }
        }


        public static class TaskImageClass implements Serializable {
            /**
             *
             */
            private static final long serialVersionUID = 1L;
            private byte[]file;
            // private ImageIcon file;
            private String filename;
            public void setFilename(String fn) {
                this.filename=fn;
            }
            public String getFilename() {
                return this.filename;
            }
            public void setFile(String file){
                try {
                    this.file=this.read(new File(file));
                }catch(Exception Ex) {
                    System.out.println(Ex.getMessage());
                }
            }
            public byte[] getFile(){
                return this.file;
            }

            private byte[] read(File file) throws IOException {


                byte []buffer = new byte[(int) file.length()];
                InputStream ios = null;
                try {
                    ios = new FileInputStream(file);
                    if ( ios.read(buffer) == -1 ) {
                        throw new IOException("EOF reached while trying to read the whole file");
                    }
                } finally {
                    try {
                        if ( ios != null )
                            ios.close();
                    } catch ( IOException e) {
                    }
                }

                return buffer;
            }


        }

        public static class returnTaskImageData implements Serializable {
            private ArrayList taskimagedata = new ArrayList();

            public void addTaskImage(TaskImageClass tidc) {
                taskimagedata.add(tidc);
            }
            public TaskImageClass getNextImage() {
                if(taskimagedata.isEmpty())
                    return null;
                else
                return (TaskImageClass)taskimagedata.remove(0);
            }
        }


        public static class returnLogin implements Serializable {
            static final long serialVersionUID = -5288831;
            private String ID;
            private boolean loginOK = false;
            private String USERROLE = "FRONTDESK";

            public void setLogin(boolean ok) {
                this.loginOK=ok;
            }

            public boolean getLogin() {
                return this.loginOK;
            }

            public void setUSERROLE(String role) {
                this.USERROLE=role;
            }

            public String getUSERROLE() {
                return this.USERROLE;
            }
            public void setID(String ID) {
                this.ID=ID;
            }

            public String getID() {
                return this.ID;
            }


        }

    }}


