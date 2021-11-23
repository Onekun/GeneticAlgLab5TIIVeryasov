package com.company;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Random rand=new Random();
        HashMap<String, String> arrGrayCode=new HashMap<>();
        //Набор ГрейКода
        arrGrayCode.put("0","0000");
        arrGrayCode.put("1","0001");
        arrGrayCode.put("2","0011");
        arrGrayCode.put("3","0010");
        arrGrayCode.put("4","0110");
        arrGrayCode.put("5","0111");
        arrGrayCode.put("6","0101");
        arrGrayCode.put("7","0100");
        arrGrayCode.put("8","1100");
        arrGrayCode.put("9","1101");
        arrGrayCode.put("10","1111");
        arrGrayCode.put("11","1110");
        arrGrayCode.put("12","1010");
        arrGrayCode.put("13","1011");
        arrGrayCode.put("14","1001");
        arrGrayCode.put("15","1000");
        HashMap<String, String> arrBinaryCode=new HashMap<>();
        //Набор Бинарных кодов
        arrBinaryCode.put("0","0000");
        arrBinaryCode.put("1","0001");
        arrBinaryCode.put("2","0010");
        arrBinaryCode.put("3","0011");
        arrBinaryCode.put("4","0100");
        arrBinaryCode.put("5","0101");
        arrBinaryCode.put("6","0110");
        arrBinaryCode.put("7","0111");
        arrBinaryCode.put("8","1000");
        arrBinaryCode.put("9","1001");
        arrBinaryCode.put("10","1010");
        arrBinaryCode.put("11","1011");
        arrBinaryCode.put("12","1100");
        arrBinaryCode.put("13","1101");
        arrBinaryCode.put("14","1110");
        arrBinaryCode.put("15","1111");
        ArrayList<Sign> signList=createFenotype(arrGrayCode, arrBinaryCode);//Определение набора признаков
        System.out.println("Признак | Десятичный | Двоичный | Код Грея ");
        for (Sign value:
             signList) {
            System.out.println(value.nameSign+"             "+value.decaValue+"          "+value.binaryValue+"       "+value.grayCode+" ");
        }
        ArrayList<Individ> individList=createInitPopulation(signList);
        individList=fitnessFunction(individList);
        System.out.println("Особь | Код особи | Приспособленность");
        for (Individ value:
                individList) {
            Integer num=Integer.parseInt(value.id)+1;
            System.out.println(num +"        "+value.DNA+"       "+value.fitness);
        }
        ArrayList<ParentsPair> parentsPairs=selection(individList);
        System.out.println("Номер пары |   Особь 1   |    Особь 2");
        for (ParentsPair value:
                parentsPairs) {
            Integer num=Integer.parseInt(String.valueOf(value.id))+1;
            Integer firstId=Integer.parseInt(value.first.id)+1;
            Integer secondId= Integer.parseInt(value.second.id)+1;
            System.out.println(num +"           "+firstId+". "+value.first.DNA+"    "+secondId+". "+value.second.DNA);
        }
        ArrayList<Individ> children=crossover(parentsPairs);
        ArrayList<Individ> mutants=mutation(individList);
        ArrayList<Individ> firstGeneration=new ArrayList<>(children);
        firstGeneration.addAll(mutants);
        System.out.println("Первое поколение");
        System.out.println("Особь | Код особи ");
        for (Individ value:
                firstGeneration) {
            Double num=Double.parseDouble(value.id);
            if(num<3)
            System.out.println(num +"        "+value.DNA);
            else {

                System.out.println(num +"(мутант)"+" "+value.DNA);
            }
        }
    }

    public static String getGrayCode(String decaValue, HashMap<String,String> arrGrayCode) {
        return arrGrayCode.get(decaValue);
    }
    public static String getBinaryCode(String decaValue, HashMap<String,String> arrBinaryCode) {
        return arrBinaryCode.get(decaValue);
    }
    public static ArrayList<Sign> createFenotype(HashMap<String,String> arrGrayCode, HashMap<String,String> arrBinaryCode){
        Random rand=new Random();
        int [] arrDecaNum=new int[5];
        //Создание десятичных значений признаков
        ArrayList<Integer> checkList=new ArrayList<>();
        for(int i=0;i<5;i++) {
            Boolean check=true;
            if(i==0){
                Integer boof = 1+rand.nextInt(14+1);
                checkList.add(boof);
                arrDecaNum[i]=boof;
            }
            else {
                while (check) {
                    Integer boof = 1+rand.nextInt(14+1);
                    FenotypeCheck fenoCheck=checkFenotype(boof, checkList);
                    if (fenoCheck.check) {
                        check = false;
                        checkList.add(boof);
                        arrDecaNum[i] = boof;
                    } else {
                        checkList.remove(fenoCheck.id);
                    }
                }
            }
        }
        ArrayList<Sign> signList=new ArrayList<>();
        //Создание набора признаков(Определение фенотипа)
        for(int i=0;i<5;i++){
            Sign sign=new Sign(String.valueOf(i+1),
                    String.valueOf(arrDecaNum[i]),
                    getGrayCode(String.valueOf(arrDecaNum[i]),arrGrayCode),
                    getBinaryCode(String.valueOf(arrDecaNum[i]),arrBinaryCode));
            signList.add(sign);
        }
        return signList;
    }
    public static FenotypeCheck checkFenotype(Integer boof, ArrayList<Integer> checkList){
        for(int i=0;i<checkList.size();i++){
            if(boof==checkList.get(i)){
                FenotypeCheck check=new FenotypeCheck(i,false);
                return check;
            }
        }
        FenotypeCheck check=new FenotypeCheck(null, true);
        return check;
    }
    public static ArrayList<Individ> createInitPopulation(ArrayList<Sign> signList){
        Random rand=new Random();
        ArrayList<Individ> individList=new ArrayList<>();
        for(int i=0;i<10;i++){
            if(i<6) {
                Individ individ=new Individ(String.valueOf(i),
                        signList.get(rand.nextInt(2)).binaryValue+signList.get(2+rand.nextInt(4-2+1)).binaryValue, 0.0);
                individList.add(individ);
            }
            else {
                Individ individ=new Individ(String.valueOf(i),
                        signList.get(rand.nextInt(2)).grayCode+signList.get(2+rand.nextInt(4-2+1)).grayCode, 0.0);
                individList.add(individ);
            }
        }
        return individList;
    }
    public static ArrayList<Individ> fitnessFunction(ArrayList<Individ> individList){
        ArrayList<Individ> individs=individList;
        ArrayList<Integer> fitnessValues=new ArrayList<>();
        Integer minSumFit=8;
        for (Individ value:
                individs) {
            Integer val=0;
            for(int i=0;i<value.DNA.length();i++){
                char symbol=value.DNA.charAt(i);
                if(Objects.equals(symbol, '1')){
                    val++;
                }
            }
            fitnessValues.add(val);
            if(minSumFit>fitnessValues.get(Integer.parseInt(value.id))) {
                minSumFit=fitnessValues.get(Integer.parseInt(value.id));
            }
        }
        for(int i=0;i<fitnessValues.size();i++){
            Individ individ=individList.get(i);
            individ.fitness=Double.valueOf(fitnessValues.get(i))/minSumFit;
            individs.set(i,individ);
        }
        return individs;
    }
    public static ArrayList<ParentsPair> selection(ArrayList<Individ> individList){
        Random rand=new Random();
        ArrayList<Individ> parentsList=new ArrayList<>();
        for(int i=0;i<individList.size();i++){
            Integer firstInd=i;
            Integer secondInd;
            if(firstInd!=individList.size()-1){
            secondInd=firstInd+1;
            }else{secondInd=rand.nextInt(8+1);}
            Individ firstParent=individList.get(firstInd);
            Individ secondParent=individList.get(secondInd);
            if(firstParent.fitness>secondParent.fitness) parentsList.add(firstParent);
            else if(firstParent.fitness==secondParent.fitness){
                    int boof=1+rand.nextInt(2);
                    if(boof==1) parentsList.add(firstParent);
                    else parentsList.add(secondParent);
            }
            else parentsList.add(secondParent);
        }
        ArrayList<ParentsPair> parentsPairs=new ArrayList<>();
        for(int i=0;i< parentsList.size()/2;i++){
            Integer first= rand.nextInt(parentsList.size());
            Individ firstInd=parentsList.get(first);
            parentsList.remove(first);
            Integer second=rand.nextInt(parentsList.size());
            Individ secondInd=parentsList.get(second);
            parentsList.remove(second);
            ParentsPair pair=new ParentsPair(i, firstInd, secondInd);
            parentsPairs.add(pair);
        }
        return parentsPairs;
    }
    public static ArrayList<Individ> crossover(ArrayList<ParentsPair> parentsPairs){
        ArrayList<Individ> children=new ArrayList<>();
        for(int i=0;i<parentsPairs.size();i++){
            String firstOne=parentsPairs.get(i).first.DNA.substring(0,2);
            String firstTwo=parentsPairs.get(i).first.DNA.substring(2,5);
            String firstThree=parentsPairs.get(i).first.DNA.substring(5,8);
            String secondOne=parentsPairs.get(i).second.DNA.substring(0,2);
            String secondTwo=parentsPairs.get(i).second.DNA.substring(2,5);
            String secondThree=parentsPairs.get(i).second.DNA.substring(5,8);
            Individ firstChild=new Individ("2."+i+1, firstOne+secondTwo+firstThree, 0.0);
            Individ secondChild=new Individ("2."+i+2, secondOne+firstTwo+secondThree, 0.0);
            children.add(firstChild);
            children.add(secondChild);
        }
        return children;
    }
    public static ArrayList<Individ> mutation(ArrayList<Individ> parents){
        Random rand=new Random();//Транслокация 3,4 и 5 генов в конец
        ArrayList<Individ> mutants=new ArrayList<>();
        for(int i=0;i<parents.size();i++){
            Integer mutationProb= rand.nextInt(100+1);
            if(mutationProb<=13){
                Individ mutant=parents.get(i);
                String first=mutant.DNA.substring(0,3);
                String mutGen=mutant.DNA.substring(3,6);
                String third=mutant.DNA.substring(6,8);
                mutant.DNA=first+third+mutGen;
                mutant.id="10."+(Integer.parseInt(parents.get(i).id)+1);
                mutants.add(mutant);
            }
        }
        return mutants;
    }
}
