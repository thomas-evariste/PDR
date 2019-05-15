import java.util.*;
class Player{
public static void main(String args[]){
ArrayList<Integer>cesNonConquises=new ArrayList<Integer>();
int i;
int j;
int k;
int l;
Tools.remplirListIDC();
Scanner in=new Scanner(System.in);
int playerCount=in.nextInt();
int myId=in.nextInt();
int zoneCount=in.nextInt();
int linkCount=in.nextInt();
int zoneId;
int platinumSource;
C pangee=new C();
for(i=0;i<zoneCount;i++){
zoneId=in.nextInt();
platinumSource=in.nextInt();
CE ce=new CE(zoneId, platinumSource);
pangee.addCE(ce);
}
int zone1;
int zone2;
for(i=0;i<linkCount;i++){
zone1=in.nextInt();
zone2=in.nextInt();
pangee.getCEById(zone1).addVoisin(zone2);
pangee.getCEById(zone2).addVoisin(zone1);
}
for(i=0;i<pangee.size();i++){
pangee.triVoisinDe(i);
}
Graphe graphe=Tools.splitC(pangee);
for(C c:graphe.getCs()){
c.calculDensitePlatinum();
c.triParPlatinum();
}
cesNonConquises=Tools.CreeCEsNonConquisesParC(graphe);
cesNonConquises=Tools.triCEsNonConquises(cesNonConquises, graphe);
ArrayList<ListD>list=CreerD.creer();
int zID;
int myPlatinum;
String deplacementStr;
CE ce;
List<Integer>deplacement=new ArrayList<Integer>();
int arrivee;
int voisin;
int nbMaxCree;
String placement;
C c;
boolean premierTour=true;
while (true){
myPlatinum=in.nextInt();
for(i=0;i<zoneCount;i++){
zID=in.nextInt();
graphe.getCEById(i).setControl(in.nextInt());
if((graphe.getCEById(i).getControl() !=-1)){
for(j=0;j<cesNonConquises.size();j++){
if(i==cesNonConquises.get(j)){
cesNonConquises.remove(j);
break;
}
}
}
for(j=0;j<4;j++){
graphe.getCEById(i).setRobots(in.nextInt(), j);
}
}
deplacementStr="";
for(j=0;j<graphe.size();j++){
c=graphe.getC(j);
for(k=0;k<c.size();k++){
ce=c.getCE(k);
if(ce.getRobots(myId) !=0){
deplacement.clear();
for(i=0;i<ce.getRobots(myId);i++){
arrivee=Tools.destination(ce.getId(), graphe);
for(l=0;l<ce.nbVoisins();l++){
voisin=ce.getVoisin(l);
if((graphe.getCEById(voisin).getControl() !=myId)
&& (!deplacement.contains(voisin))){
arrivee=voisin;
deplacement.add(arrivee);
break;
}
}
deplacementStr=deplacementStr+" "+1+" "+ce.getId()+" "+arrivee;
}
}
}
}
if(deplacementStr==""){
deplacementStr="WAIT";
}
System.out.println(deplacementStr);
nbMaxCree=myPlatinum / 20;
placement="";
placement=Tools.nouveauPlacement(nbMaxCree, playerCount, cesNonConquises, graphe, myId, premierTour);
if(placement==""){
placement="WAIT";
}
System.out.println(placement);
if(premierTour){
premierTour=false;
}
}
}
}
class Tools{
static List<Integer>jap;
static List<Integer>ameriqueDuN;
static List<Integer>ameriqueDuS;
static List<Integer>antarct;
static int destination(int idCE, Graphe graphe){
Random rand=new Random();
int arrivee=graphe.getCEById(idCE)
.getVoisin(rand.nextInt(graphe.getCEById(idCE).nbVoisins()));
return arrivee;
}
static int positionAlea(Graphe graphe){
Random rand=new Random();
int pos=graphe.getCE(rand.nextInt(graphe.sizeCE())).getId();
return pos;
}
static Graphe splitC(C c){
Graphe graphe=new Graphe();
C ameriqueDuNord=new C(0);
C ameriqueDuSudAfrique=new C(1);
C antarctique=new C(2);
C europeAsieOceanie=new C(3);
C japon=new C(4);
CE ce;
int i;
for(i=0;i<c.size();i++){
ce=c.getCE(i);
int id=ce.getId();
if(ameriqueDuS.contains(id)){
ameriqueDuSudAfrique.addCE(ce);
} else if(ameriqueDuN.contains(id)){
ameriqueDuNord.addCE(ce);
} else if(antarct.contains(id)){
antarctique.addCE(ce);
} else if(jap.contains(id)){
japon.addCE(ce);
} else{
europeAsieOceanie.addCE(ce);
}
}
graphe.addC(ameriqueDuNord);
graphe.addC(ameriqueDuSudAfrique);
graphe.addC(antarctique);
graphe.addC(europeAsieOceanie);
graphe.addC(japon);
return graphe;
}
static void remplirListIDC(){
Integer[] j=new Integer[]{ 143, 149, 150 };
Integer[] an=new Integer[]{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 19, 20, 21, 22, 23,
27, 28, 29, 37, 38, 43, 46, 47, 48, 49 };
Integer[] as=new Integer[]{ 18, 24, 25, 26, 30, 31, 32, 33, 34, 35, 36, 39, 40, 41, 42, 44, 45, 50, 51, 54,
55, 56, 60, 61, 62, 63, 64, 65, 66, 71, 72, 73, 74, 75, 76, 77, 83, 84, 85, 86, 87, 88, 95, 96 };
Integer[] ant=new Integer[]{ 57, 67, 78, 89, 97, 104, 113 };
jap=Arrays.asList(j);
ameriqueDuN=Arrays.asList(an);
ameriqueDuS=Arrays.asList(as);
antarct=Arrays.asList(ant);
}
static void miseAJourC(Graphe graphe){
ArrayList<Integer>supp=new ArrayList<Integer>();
C c;
int j;
for(j=0;j<graphe.size();j++){
c=graphe.getC(j);
if(c.verifPoss()){
supp.add(c.getId());
}
}
int s;
for(j=0;j<supp.size();j++){
s=supp.get(j);
graphe.removeCById(s);
}
}
static void miseAJourGraphe(Graphe graphe){
miseAJourC(graphe);
}
static int takeRandom(ArrayList<Integer>liste){
int taille=liste.size();
Random rand=new Random();
int hasard=rand.nextInt(taille);
return liste.get(hasard);
}
static String nouveauPlacement(int nbMaxCree, int playerCount, ArrayList<Integer>cesNonConquises,
Graphe graphe, int myId, boolean premierTour){
String sortie="";
if(playerCount==2){
if(premierTour){
sortie=nouveauPlacement1v1PremierTour(nbMaxCree, cesNonConquises, graphe, myId);
} else{
if(premierTour){
sortie=nouveauPlacementMultiPremierTour(nbMaxCree, cesNonConquises, graphe, myId);
} else{
sortie=nouveauPlacementMulti(nbMaxCree, cesNonConquises, graphe, myId);
}
}
} else{
if(premierTour){
sortie=nouveauPlacementMultiPremierTour(nbMaxCree, cesNonConquises, graphe, myId);
} else{
sortie=nouveauPlacementMulti(nbMaxCree, cesNonConquises, graphe, myId);
}
}
return sortie;
}
static String nouveauPlacement1v1(int nbMaxCree, ArrayList<Integer>cesNonConquises, Graphe graphe,
int myId){
String sortie="";
int compteur=0;
while (nbMaxCree !=0){
if((!cesNonConquises.isEmpty()) && cesNonConquises.size()>compteur){
sortie=sortie+" 1 "+String.valueOf(cesNonConquises.get(compteur));
nbMaxCree--;
compteur++;
} else{
sortie=sortie+" 1 "+String.valueOf(Tools.positionAleaV2(graphe, myId, true));
nbMaxCree--;
}
}
return sortie;
}
static String nouveauPlacementMultiPremierTour(int nbMaxCree, ArrayList<Integer>cesNonConquises,
Graphe graphe, int myId){
String sortie="";
for(int i=0;i<nbMaxCree;i++){
sortie=sortie+" 1 "+String.valueOf(Tools.takeProcheGrandeMine(cesNonConquises, i, graphe));
}
return sortie;
}
static ArrayList<Integer>triCEsNonConquises(ArrayList<Integer>cesNonConquises, Graphe graphe){
ArrayList<Integer>tableauTrie=new ArrayList<Integer>();
int i;
int j;
for(i=0;i<7;i++){
for(j=0;j<cesNonConquises.size();j++){
if(graphe.getCEById(cesNonConquises.get(j)).getPlatinum()==(6 - i)){
tableauTrie.add(cesNonConquises.get(j));
}
}
}
return tableauTrie;
}
static int positionAleaV2(Graphe graphe, int myId, Boolean bool){
Graphe grapheLocal=new Graphe(graphe);
if(bool){
for(C c:graphe.getCs()){
if(!c.estExploitable(myId)){
grapheLocal.removeCById(c.getId());
}
}
}
ArrayList<Integer>mesCEs=new ArrayList<Integer>();
for(C c:grapheLocal.getCs()){
for(CE ce:c.getCEs()){
if(ce.getControl()==myId){
mesCEs.add(ce.getId());
}
}
}
ArrayList<Integer>voisins=new ArrayList<Integer>();
ArrayList<Integer>mesCEsEnBordure=new ArrayList<Integer>();
Boolean unVoisinEnnemi=false;
for(int i=0;i<mesCEs.size();i++){
unVoisinEnnemi=false;
voisins=graphe.getCEById(mesCEs.get(i)).getVoisins();
for(int voisin:voisins){
if(graphe.getCEById(voisin).getControl() !=myId){
unVoisinEnnemi=true;
}
}
if(unVoisinEnnemi){
mesCEsEnBordure.add(mesCEs.get(i));
}
}
if(!mesCEsEnBordure.isEmpty()){
mesCEs=mesCEsEnBordure;
}
Random rand=new Random();
int id=mesCEs.get(rand.nextInt(mesCEs.size()));
return id;
}
static int takeProcheGrandeMine(ArrayList<Integer>cesNonConquises, int parcourtNonConquis,
Graphe graphe){
CE maCE=graphe.getCEById(cesNonConquises.get(parcourtNonConquis));
for(int voisin:maCE.getVoisins()){
if(graphe.getCEById(voisin).getControl()==-1){
return voisin;
}
}
return maCE.getId();
}
static String nouveauPlacement1v1PremierTour(int nbMaxCree, ArrayList<Integer>cesNonConquises,
Graphe graphe, int myId){
String sortie="";
int compteur=0;
for(int i=0;i<3;i++){
sortie=sortie+" 2 "+String.valueOf(cesNonConquises.get(i));
}
for(int i=3;i<7;i++){
sortie=sortie+" 1 "+String.valueOf(cesNonConquises.get(i));
}
return sortie;
}
static String nouveauPlacementMulti(int nbMaxCree, ArrayList<Integer>cesNonConquises, Graphe graphe,
int myId){
String sortie="";
int parcourtNonConquis=0;
for(int i=0;i<nbMaxCree;i++){
if(cesNonConquises.isEmpty()){
sortie=sortie+" 1 "+String.valueOf(Tools.positionAleaV2(graphe, myId, true));
}
if(parcourtNonConquis>=cesNonConquises.size()){
parcourtNonConquis=0;
}
else{
sortie=sortie+" 1 "
+String.valueOf(Tools.takeProcheGrandeMine(cesNonConquises, parcourtNonConquis, graphe));
parcourtNonConquis++;
}
}
return sortie;
}
static ArrayList<Integer>CreeCEsNonConquisesParC(Graphe graphe){
ArrayList<Integer>cesNonConquises=new ArrayList<Integer>();
ArrayList<Integer>idCsClasses=new ArrayList<Integer>();
ArrayList<Integer>idCs=new ArrayList<Integer>();
idCs.add(0);
idCs.add(1);
idCs.add(2);
idCs.add(3);
idCs.add(4);
double max=-1;
int idCMax=-1;
int compteur=0;
int compteurFinal=-1;
while (!idCs.isEmpty()){
for(int n:idCs){
if(graphe.getCById(n).getDensitePlatinum()>=max){
idCMax=n;
max=graphe.getCById(n).getDensitePlatinum();
compteurFinal=compteur;
}
compteur++;
}
idCsClasses.add(idCMax);
System.err.println(""+idCMax);
idCs.remove(compteurFinal);
max=-1;
idCMax=-1;
compteur=0;
compteurFinal=-1;
}
for(int idC:idCsClasses){
for(CE uneCE:graphe.getCById(idC).getCEs()){
cesNonConquises.add(uneCE.getId());
}
}
return cesNonConquises;
}
}
class Graphe{
private ArrayList<C>cs;
Graphe(){
cs=new ArrayList<C>();
}
Graphe(Graphe graphe){
cs=new ArrayList<C>();
C c;
int j;
for(j=0;j<graphe.size();j++){
c=graphe.getC(j);
cs.add(new C(c));
}
}
ArrayList<C>getCs(){
return cs;
}
C getC(int num){
return cs.get(num);
}
C getCById(int id){
C cVide=new C();
C c;
int j;
for(j=0;j<cs.size();j++){
c=cs.get(j);
if(id==c.getId()){
return c;
}
}
System.err.println("c pas dans le graphe:"+id);
return cVide;
}
void setCs(ArrayList<C>cs){
this.cs=new ArrayList<C>(cs);
}
void addC(C c){
cs.add(c);
}
void addCs(ArrayList<C>cs){
C c;
int j;
for(j=0;j<cs.size();j++){
c=cs.get(j);
this.addC(c);
}
}
void removeC(int num){
cs.remove(num);
}
void removeCById(int id){
int i;
for(i=0;i<cs.size();i++){
if(cs.get(i).getId()==id){
cs.remove(i);
i--;
}
}
}
int size(){
return cs.size();
}
int sizeCE(){
int taille=0;
C c;
int j;
for(j=0;j<cs.size();j++){
c=cs.get(j);
taille+=c.size();
}
return taille;
}
Boolean isEmptyC(){
return cs.isEmpty();
}
Boolean isEmptyCase(){
C c;
int j;
for(j=0;j<cs.size();j++){
c=cs.get(j);
if(!c.isEmpty()){
return false;
}
}
return true;
}
CE getCEById(int id){
CE ceVide=new CE();
C c;
CE ce;
int j;
for(j=0;j<cs.size();j++){
c=cs.get(j);
int k;
for(k=0;k<c.size();k++){
ce=c.getCE(k);
if(id==ce.getId()){
return ce;
}
}
}
System.err.println("case pas dans le graphe:"+id);
return ceVide;
}
CE getCE(int num){
CE ceVide=new CE();
int comptNum=0;
C c;
CE ce;
int j;
for(j=0;j<cs.size();j++){
c=cs.get(j);
int k;
for(k=0;k<c.size();k++){
ce=c.getCE(k);
if(num==comptNum){
return ce;
}
comptNum++;
}
}
System.err.println("pas assez de ces:"+num);
System.err.println(""+comptNum);
return ceVide;
}
void removeCEById(int id){
int idC=-1;
C c;
CE ce;
int j;
for(j=0;j<cs.size();j++){
c=cs.get(j);
int k;
for(k=0;k<c.size();k++){
ce=c.getCE(k);
if(id==ce.getId()){
idC=c.getId();
}
}
}
cs.get(idC).removeCEById(id);
}
}
class C{
private ArrayList<CE>ces;
private int id;
private double densitePlatinum;
C(){
ces=new ArrayList<CE>();
id=-1;
densitePlatinum=0;
}
C(int id){
ces=new ArrayList<CE>();
this.id=id;
densitePlatinum=0;
}
C(ArrayList<CE>ces){
this.ces=ces;
id=-1;
densitePlatinum=0;
}
C(ArrayList<CE>ces, int id){
this.ces=ces;
this.id=id;
densitePlatinum=0;
}
C(C c){
ces=new ArrayList<CE>(c.getCEs());
id=c.getId();
densitePlatinum=0;
}
ArrayList<CE>getCEs(){
return ces;
}
CE getCE(int num){
return ces.get(num);
}
double getDensitePlatinum(){
return densitePlatinum;
}
CE getCEById(int id){
CE ceVide=new CE();
CE ce;
int j;
for(j=0;j<ces.size();j++){
ce=ces.get(j);
if(id==ce.getId()){
return ce;
}
}
return ceVide;
}
void setCEs(ArrayList<CE>ces){
this.ces=ces;
}
void setDensitePlatinum(double d){
densitePlatinum=d;
}
void addCE(CE ce){
ces.add(ce);
}
void addCEs(ArrayList<CE>ces){
CE ce;
int j;
for(j=0;j<ces.size();j++){
ce=ces.get(j);
this.addCE(ce);
}
}
void removeCE(int num){
ces.remove(num);
}
void removeCEById(int id){
int i;
for(i=0;i<ces.size();i++){
if(ces.get(i).getId()==id){
ces.remove(i);
i--;
}
}
}
int size(){
return ces.size();
}
Boolean isEmpty(){
return ces.isEmpty();
}
void triVoisinDe(int id){ 
ArrayList<Integer>voisin=this.getCEById(id).getVoisins();
ArrayList<Integer>voisinTrie=new ArrayList<Integer>();
int i;
int j;
for(i=0;i<7;i++){
for(j=voisin.size()-1;j>=0;j--){
if(this.getCEById(voisin.get(j)).getPlatinum()==(6 - i)){
voisinTrie.add(voisin.get(j));
}
}
}
this.getCEById(id).setVoisins(voisinTrie);
}
void setId(int id){
this.id=id;
}
int getId(){
return id;
}
Boolean verifPoss(){
int i;
for(i=0;i<ces.size() - 1;i++){
if(ces.get(i).getControl() !=ces.get(i+1).getControl()){
return false;
}
}
if(ces.get(0).getControl()==-1){
return false;
}
return true;
}
void calculDensitePlatinum(){
double comptPlatinum=0;
CE ce;
int j;
for(j=0;j<ces.size();j++){
ce=ces.get(j);
comptPlatinum+=ce.getPlatinum();
}
double densitePlatinum=comptPlatinum / ((double) ces.size());
this.densitePlatinum=densitePlatinum;
}
void triParPlatinum(){
ArrayList<CE>cesTriees=new ArrayList<CE>();
int i;
int j;
CE ce;
for(i=0;i<7;i++){
for(j=0;j<ces.size();j++){
ce=ces.get(j);
if(ce.getPlatinum()==(6 - i)){
cesTriees.add(ce);
}
}
}
ces=cesTriees;
}
int nombreDeCEsControlees(){
int compteur=0;
for(int i=0;i<ces.size();i++){
if(getCEById(i).getControl() !=-1){
compteur++;
}
}
return compteur;
}
Boolean estExploitable(int myId){
int compteur[]=new int[2];
for(int i=0;i<2;i++){
compteur[i]=0;
}
for(int j=0;j<ces.size();j++){
if(getCE(j).getControl() !=-1){
if(getCE(j).getControl()==myId){
compteur[0]++;
}
else{
compteur[1]++;
}
}
}
for(int k=0;k<2;k++){
if(compteur[k]==ces.size()){
return false;
}
}
return true;
}
}
class CE{
private int id;
private int controlePar;
private int[] robots;
private int platinum;
private ArrayList<Integer>voisins;
CE(int id, int platinum, ArrayList<Integer>voisins){
this.id=id;
this.platinum=platinum;
this.voisins=voisins;
controlePar=-1;
robots=new int[4];
int i;
for(i=0;i<4;i++){
robots[i]=0;
}
}
CE(int id, int platinum){
this.id=id;
this.platinum=platinum;
voisins=new ArrayList<Integer>();
controlePar=-1;
robots=new int[4];
int i;
for(i=0;i<4;i++){
robots[i]=0;
}
}
CE(){
id=-1;
platinum=-1;
voisins=new ArrayList<Integer>();
controlePar=-1;
robots=new int[4];
int i;
for(i=0;i<4;i++){
robots[i]=0;
}
}
int getId(){
return id;
}
int getControl(){
return controlePar;
}
int getPlatinum(){
return platinum;
}
ArrayList<Integer>getVoisins(){
return voisins;
}
int getVoisin(int num){
return voisins.get(num);
}
int getRobots(int i){
return robots[i];
}
Boolean verifVoisin(int id){
return voisins.contains(id);
}
void setId(int id){
this.id=id;
}
void setControl(int numJ){
controlePar=numJ;
}
void setPlatinum(int platinum){
this.platinum=platinum;
}
void setVoisins(ArrayList<Integer>voisins){
this.voisins=voisins;
}
void setRobots(int nombre, int i){
this.robots[i]=nombre;
}
void addVoisin(int idVoisin){
voisins.add(idVoisin);
}
void addVoisinA(int idVoisin, int pos){
voisins.add(pos, idVoisin);
}
void suppVoisin(int pos){
voisins.remove(pos);
}
void videVoisin(){
voisins.clear();
}
int nbVoisins(){
return voisins.size();
}
}
class ListD{
private D[] distances;
ListD(D[] distances){
this.distances=distances;
}
void set(D[] distances){
this.distances=distances;
}
D[] getDistances(){
return distances;
}
D getDistance(int num){
return distances[num];
}
}
class D{
private int id;
private int[][] dists;
D(){
id=-1;
}
D(int id){
this.id=id;
}
D(int id, int[][] dists){
this.id=id;
this.dists=dists;
}
void setId(int id){
this.id=id;
}
void setDists(int[][] dists){
this.dists=dists;
}
int[][] getDists(){
return dists;
}
int[] getDist(int num){
return dists[num];
}
int getId(){
return id;
}
int size(){
return dists.length;
}
}
class CreerD{
static ArrayList<ListD>creer(){
ArrayList<ListD>list=new ArrayList<ListD>();
int[][] b0={{150},{149}};
D d0=new D(143,b0);
int[][] b1={{150},{143}};
D d1=new D(149,b1);
int[][] b2={{143,149}};
D d2=new D(150,b2);
D[] di0={d0,d1,d2};
ListD listD1=new ListD(di0);
list.add(listD1) ;
int[][] b3={{67},{78},{89},{97},{104},{113}};
D d3=new D(57,b3);
int[][] b4={{57,78},{89},{97},{104},{113}};
D d4=new D(67,b4);
int[][] b5={{67,89},{57,97},{104},{113}};
D d5=new D(78,b5);
int[][] b6={{78,97},{67,104},{57,113}};
D d6=new D(89,b6);
int[][] b7={{89,104},{78,113},{67},{57}};
D d7=new D(97,b7);
int[][] b8={{97,113},{89},{78},{67},{57}};
D d8=new D(104,b8);
int[][] b9={{104},{97},{89},{78},{67},{57}};
D d9=new D(113,b9);
D[] di1={d3,d4,d5,d6,d7,d8,d9};
ListD listD2=new ListD(di1);
list.add(listD2) ;
int[][] b10={{1},{2,3},{4,7,8},{5,9,13,14,15},{6,10,16,19,20,21,22},{11,17,23,27,28,29},{12,37,38},{43},{46},{47,48,49}};
D d10=new D(0,b10);
int[][] b11={{0,2,3},{4,7,8},{5,9,13,14,15},{6,10,16,19,20,21,22},{11,17,23,27,28,29},{12,37,38},{43},{46},{47,48,49}};
D d11=new D(1,b11);
int[][] b12={{1,3,7},{0,4,8,13,14},{5,9,15,19,20,21},{6,10,16,22,27,28,29},{11,17,23,37,38},{12,43},{46},{47,48,49}};
D d12=new D(2,b12);
int[][] b13={{1,2,4,7,8},{0,5,9,13,14,15},{6,10,16,19,20,21,22},{11,17,23,27,28,29},{12,37,38},{43},{46},{47,48,49}};
D d13=new D(3,b13);
int[][] b14={{3,5,8,9},{1,2,6,7,10,14,15,16},{0,11,13,17,20,21,22,23},{12,19,27,28,29},{37,38},{43},{46},{47,48,49}};
D d14=new D(4,b14);
int[][] b15={{4,6,9,10},{3,8,11,15,16,17},{1,2,7,12,14,21,22,23},{0,13,20,28,29},{19,27,38},{37},{43},{46},{47,48,49}};
D d15=new D(5,b15);
int[][] b16={{5,10,11},{4,9,12,16,17},{3,8,15,22,23},{1,2,7,14,21,29},{0,13,20,28,38},{19,27},{37},{43},{46},{47,48,49}};
D d16=new D(6,b16);
int[][] b17={{2,3,8,13,14},{1,4,9,15,19,20,21},{0,5,10,16,22,27,28,29},{6,11,17,23,37,38},{12,43},{46},{47,48,49}};
D d17=new D(7,b17);
int[][] b18={{3,4,7,9,14,15},{1,2,5,10,13,16,20,21,22},{0,6,11,17,19,23,27,28,29},{12,37,38},{43},{46},{47,48,49}};
D d18=new D(8,b18);
int[][] b19={{4,5,8,10,15,16},{3,6,7,11,14,17,21,22,23},{1,2,12,13,20,28,29},{0,19,27,38},{37},{43},{46},{47,48,49}};
D d19=new D(9,b19);
int[][] b20={{5,6,9,11,16,17},{4,8,12,15,22,23},{3,7,14,21,29},{1,2,13,20,28,38},{0,19,27},{37},{43},{46},{47,48,49}};
D d20=new D(10,b20);
int[][] b21={{6,10,12,17},{5,9,16,23},{4,8,15,22},{3,7,14,21,29},{1,2,13,20,28,38},{0,19,27},{37},{43},{46},{47,48,49}};
D d21=new D(11,b21);
int[][] b22={{11},{6,10,17},{5,9,16,23},{4,8,15,22},{3,7,14,21,29},{1,2,13,20,28,38},{0,19,27},{37},{43},{46},{47,48,49}};
D d22=new D(12,b22);
int[][] b23={{7,14,19,20},{2,3,8,15,21,27,28},{1,4,9,16,22,29,37,38},{0,5,10,17,23,43},{6,11,46},{12,47,48,49}};
D d23=new D(13,b23);
int[][] b24={{7,8,13,15,20,21},{2,3,4,9,16,19,22,27,28,29},{1,5,10,17,23,37,38},{0,6,11,43},{12,46},{47,48,49}};
D d24=new D(14,b24);
int[][] b25={{8,9,14,16,21,22},{3,4,5,7,10,13,17,20,23,28,29},{1,2,6,11,19,27,38},{0,12,37},{43},{46},{47,48,49}};
D d25=new D(15,b25);
int[][] b26={{9,10,15,17,22,23},{4,5,6,8,11,14,21,29},{3,7,12,13,20,28,38},{1,2,19,27},{0,37},{43},{46},{47,48,49}};
D d26=new D(16,b26);
int[][] b27={{10,11,16,23},{5,6,9,12,15,22},{4,8,14,21,29},{3,7,13,20,28,38},{1,2,19,27},{0,37},{43},{46},{47,48,49}};
D d27=new D(17,b27);
int[][] b28={{13,20,27},{7,14,21,28,37},{2,3,8,15,22,29,38,43},{1,4,9,16,23,46},{0,5,10,17,47,48,49},{6,11},{12}};
D d28=new D(19,b28);
int[][] b29={{13,14,19,21,27,28},{7,8,15,22,29,37,38},{2,3,4,9,16,23,43},{1,5,10,17,46},{0,6,11,47,48,49},{12}};
D d29=new D(20,b29);
int[][] b30={{14,15,20,22,28,29},{7,8,9,13,16,19,23,27,38},{2,3,4,5,10,17,37},{1,6,11,43},{0,12,46},{47,48,49}};
D d30=new D(21,b30);
int[][] b31={{15,16,21,23,29},{8,9,10,14,17,20,28,38},{3,4,5,6,7,11,13,19,27},{1,2,12,37},{0,43},{46},{47,48,49}};
D d31=new D(22,b31);
int[][] b32={{16,17,22},{9,10,11,15,21,29},{4,5,6,8,12,14,20,28,38},{3,7,13,19,27},{1,2,37},{0,43},{46},{47,48,49}};
D d32=new D(23,b32);
int[][] b33={{19,20,28,37},{13,14,21,29,38,43},{7,8,15,22,46},{2,3,4,9,16,23,47,48,49},{1,5,10,17},{0,6,11},{12}};
D d33=new D(27,b33);
int[][] b34={{20,21,27,29,38},{13,14,15,19,22,37},{7,8,9,16,23,43},{2,3,4,5,10,17,46},{1,6,11,47,48,49},{0,12}};
D d34=new D(28,b34);
int[][] b35={{21,22,28,38},{14,15,16,20,23,27},{7,8,9,10,13,17,19,37},{2,3,4,5,6,11,43},{1,12,46},{0,47,48,49}};
D d35=new D(29,b35);
int[][] b36={{27,43},{19,20,28,46},{13,14,21,29,38,47,48,49},{7,8,15,22},{2,3,4,9,16,23},{1,5,10,17},{0,6,11},{12}};
D d36=new D(37,b36);
int[][] b37={{28,29},{20,21,22,27},{13,14,15,16,19,23,37},{7,8,9,10,17,43},{2,3,4,5,6,11,46},{1,12,47,48,49},{0}};
D d37=new D(38,b37);
int[][] b38={{37,46},{27,47,48,49},{19,20,28},{13,14,21,29,38},{7,8,15,22},{2,3,4,9,16,23},{1,5,10,17},{0,6,11},{12}};
D d38=new D(43,b38);
int[][] b39={{43,47,48,49},{37},{27},{19,20,28},{13,14,21,29,38},{7,8,15,22},{2,3,4,9,16,23},{1,5,10,17},{0,6,11},{12}};
D d39=new D(46,b39);
int[][] b40={{46,49},{43,48},{37},{27},{19,20,28},{13,14,21,29,38},{7,8,15,22},{2,3,4,9,16,23},{1,5,10,17},{0,6,11},{12}};
D d40=new D(47,b40);
int[][] b41={{46,49},{43,47},{37},{27},{19,20,28},{13,14,21,29,38},{7,8,15,22},{2,3,4,9,16,23},{1,5,10,17},{0,6,11},{12}};
D d41=new D(48,b41);
int[][] b42={{46,47,48},{43},{37},{27},{19,20,28},{13,14,21,29,38},{7,8,15,22},{2,3,4,9,16,23},{1,5,10,17},{0,6,11},{12}};
D d42=new D(49,b42);
D[] di2={d10,d11,d12,d13,d14,d15,d16,d17,d18,d19,d20,d21,d22,d23,d24,d25,d26,d27,d28,d29,d30,d31,d32,d33,d34,d35,d36,d37,d38,d39,d40,d41,d42};
ListD listD3=new ListD(di2);
list.add(listD3) ;
int[][] b43={{44,51},{39,40,45,50,55,56},{30,31,32,41,54,61,62,63},{24,25,26,33,42,60,64,71,72,73,74},{34,65,75,83,84,85,86},{35,66,76,87,95,96},{36,77,88}};
D d43=new D(18,b43);
int[][] b44={{25,30},{26,31,39},{32,40,44},{18,33,41,45},{34,42,51},{35,50,55,56},{36,54,61,62,63},{60,64,71,72,73,74},{65,75,83,84,85,86},{66,76,87,95,96},{77,88}};
D d44=new D(24,b44);
int[][] b45={{24,26,30,31},{32,39,40},{33,41,44,45},{18,34,42},{35,51},{36,50,55,56},{54,61,62,63},{60,64,71,72,73,74},{65,75,83,84,85,86},{66,76,87,95,96},{77,88}};
D d45=new D(25,b45);
int[][] b46={{25,31,32},{24,30,33,39,40,41},{34,42,44,45},{18,35},{36,51},{50,55,56},{54,61,62,63},{60,64,71,72,73,74},{65,75,83,84,85,86},{66,76,87,95,96},{77,88}};
D d46=new D(26,b46);
int[][] b47={{24,25,31,39},{26,32,40,44},{18,33,41,45},{34,42,51},{35,50,55,56},{36,54,61,62,63},{60,64,71,72,73,74},{65,75,83,84,85,86},{66,76,87,95,96},{77,88}};
D d47=new D(30,b47);
int[][] b48={{25,26,30,32,39,40},{24,33,41,44,45},{18,34,42},{35,51},{36,50,55,56},{54,61,62,63},{60,64,71,72,73,74},{65,75,83,84,85,86},{66,76,87,95,96},{77,88}};
D d48=new D(31,b48);
int[][] b49={{26,31,33,40,41},{25,30,34,39,42,44,45},{18,24,35},{36,51},{50,55,56},{54,61,62,63},{60,64,71,72,73,74},{65,75,83,84,85,86},{66,76,87,95,96},{77,88}};
D d49=new D(32,b49);
int[][] b50={{32,34,41,42},{26,31,35,40,45},{25,30,36,39,44},{18,24},{51},{50,55,56},{54,61,62,63},{60,64,71,72,73,74},{65,75,83,84,85,86},{66,76,87,95,96},{77,88}};
D d50=new D(33,b50);
int[][] b51={{33,35,42},{32,36,41},{26,31,40,45},{25,30,39,44},{18,24},{51},{50,55,56},{54,61,62,63},{60,64,71,72,73,74},{65,75,83,84,85,86},{66,76,87,95,96},{77,88}};
D d51=new D(34,b51);
int[][] b52={{34,36},{33,42},{32,41},{26,31,40,45},{25,30,39,44},{18,24},{51},{50,55,56},{54,61,62,63},{60,64,71,72,73,74},{65,75,83,84,85,86},{66,76,87,95,96},{77,88}};
D d52=new D(35,b52);
int[][] b53={{35},{34},{33,42},{32,41},{26,31,40,45},{25,30,39,44},{18,24},{51},{50,55,56},{54,61,62,63},{60,64,71,72,73,74},{65,75,83,84,85,86},{66,76,87,95,96},{77,88}};
D d53=new D(36,b53);
int[][] b54={{30,31,40,44},{18,24,25,26,32,41,45},{33,42,51},{34,50,55,56},{35,54,61,62,63},{36,60,64,71,72,73,74},{65,75,83,84,85,86},{66,76,87,95,96},{77,88}};
D d54=new D(39,b54);
int[][] b55={{31,32,39,41,44,45},{18,25,26,30,33,42},{24,34,51},{35,50,55,56},{36,54,61,62,63},{60,64,71,72,73,74},{65,75,83,84,85,86},{66,76,87,95,96},{77,88}};
D d55=new D(40,b55);
int[][] b56={{32,33,40,42,45},{26,31,34,39,44},{18,25,30,35},{24,36,51},{50,55,56},{54,61,62,63},{60,64,71,72,73,74},{65,75,83,84,85,86},{66,76,87,95,96},{77,88}};
D d56=new D(41,b56);
int[][] b57={{33,34,41},{32,35,40,45},{26,31,36,39,44},{18,25,30},{24,51},{50,55,56},{54,61,62,63},{60,64,71,72,73,74},{65,75,83,84,85,86},{66,76,87,95,96},{77,88}};
D d57=new D(42,b57);
int[][] b58={{18,39,40,45},{30,31,32,41,51},{24,25,26,33,42,50,55,56},{34,54,61,62,63},{35,60,64,71,72,73,74},{36,65,75,83,84,85,86},{66,76,87,95,96},{77,88}};
D d58=new D(44,b58);
int[][] b59={{40,41,44},{18,31,32,33,39,42},{25,26,30,34,51},{24,35,50,55,56},{36,54,61,62,63},{60,64,71,72,73,74},{65,75,83,84,85,86},{66,76,87,95,96},{77,88}};
D d59=new D(45,b59);
int[][] b60={{51,54,55},{18,56,60,61,62},{44,63,71,72,73},{39,40,45,64,74,83,84,85},{30,31,32,41,65,75,86,95,96},{24,25,26,33,42,66,76,87},{34,77,88},{35},{36}};
D d60=new D(50,b60);
int[][] b61={{18,50,55,56},{44,54,61,62,63},{39,40,45,60,64,71,72,73,74},{30,31,32,41,65,75,83,84,85,86},{24,25,26,33,42,66,76,87,95,96},{34,77,88},{35},{36}};
D d61=new D(51,b61);
int[][] b62={{50,55,60,61},{51,56,62,71,72},{18,63,73,83,84},{44,64,74,85,95,96},{39,40,45,65,75,86},{30,31,32,41,66,76,87},{24,25,26,33,42,77,88},{34},{35},{36}};
D d62=new D(54,b62);
int[][] b63={{50,51,54,56,61,62},{18,60,63,71,72,73},{44,64,74,83,84,85},{39,40,45,65,75,86,95,96},{30,31,32,41,66,76,87},{24,25,26,33,42,77,88},{34},{35},{36}};
D d63=new D(55,b63);
int[][] b64={{51,55,62,63},{18,50,54,61,64,72,73,74},{44,60,65,71,75,83,84,85,86},{39,40,45,66,76,87,95,96},{30,31,32,41,77,88},{24,25,26,33,42},{34},{35},{36}};
D d64=new D(56,b64);
int[][] b65={{54,61,71},{50,55,62,72,83},{51,56,63,73,84,95},{18,64,74,85,96},{44,65,75,86},{39,40,45,66,76,87},{30,31,32,41,77,88},{24,25,26,33,42},{34},{35},{36}};
D d65=new D(60,b65);
int[][] b66={{54,55,60,62,71,72},{50,51,56,63,73,83,84},{18,64,74,85,95,96},{44,65,75,86},{39,40,45,66,76,87},{30,31,32,41,77,88},{24,25,26,33,42},{34},{35},{36}};
D d66=new D(61,b66);
int[][] b67={{55,56,61,63,72,73},{50,51,54,60,64,71,74,83,84,85},{18,65,75,86,95,96},{44,66,76,87},{39,40,45,77,88},{30,31,32,41},{24,25,26,33,42},{34},{35},{36}};
D d67=new D(62,b67);
int[][] b68={{56,62,64,73,74},{51,55,61,65,72,75,84,85,86},{18,50,54,60,66,71,76,83,87,95,96},{44,77,88},{39,40,45},{30,31,32,41},{24,25,26,33,42},{34},{35},{36}};
D d68=new D(63,b68);
int[][] b69={{63,65,74,75},{56,62,66,73,76,85,86,87},{51,55,61,72,77,84,88,96},{18,50,54,60,71,83,95},{44},{39,40,45},{30,31,32,41},{24,25,26,33,42},{34},{35},{36}};
D d69=new D(64,b69);
int[][] b70={{64,66,75,76},{63,74,77,86,87,88},{56,62,73,85},{51,55,61,72,84,96},{18,50,54,60,71,83,95},{44},{39,40,45},{30,31,32,41},{24,25,26,33,42},{34},{35},{36}};
D d70=new D(65,b70);
int[][] b71={{65,76,77},{64,75,87,88},{63,74,86},{56,62,73,85},{51,55,61,72,84,96},{18,50,54,60,71,83,95},{44},{39,40,45},{30,31,32,41},{24,25,26,33,42},{34},{35},{36}};
D d71=new D(66,b71);
int[][] b72={{60,61,72,83},{54,55,62,73,84,95},{50,51,56,63,74,85,96},{18,64,75,86},{44,65,76,87},{39,40,45,66,77,88},{30,31,32,41},{24,25,26,33,42},{34},{35},{36}};
D d72=new D(71,b72);
int[][] b73={{61,62,71,73,83,84},{54,55,56,60,63,74,85,95,96},{50,51,64,75,86},{18,65,76,87},{44,66,77,88},{39,40,45},{30,31,32,41},{24,25,26,33,42},{34},{35},{36}};
D d73=new D(72,b73);
int[][] b74={{62,63,72,74,84,85},{55,56,61,64,71,75,83,86,95,96},{50,51,54,60,65,76,87},{18,66,77,88},{44},{39,40,45},{30,31,32,41},{24,25,26,33,42},{34},{35},{36}};
D d74=new D(73,b74);
int[][] b75={{63,64,73,75,85,86},{56,62,65,72,76,84,87,96},{51,55,61,66,71,77,83,88,95},{18,50,54,60},{44},{39,40,45},{30,31,32,41},{24,25,26,33,42},{34},{35},{36}};
D d75=new D(74,b75);
int[][] b76={{64,65,74,76,86,87},{63,66,73,77,85,88},{56,62,72,84,96},{51,55,61,71,83,95},{18,50,54,60},{44},{39,40,45},{30,31,32,41},{24,25,26,33,42},{34},{35},{36}};
D d76=new D(75,b76);
int[][] b77={{65,66,75,77,87,88},{64,74,86},{63,73,85},{56,62,72,84,96},{51,55,61,71,83,95},{18,50,54,60},{44},{39,40,45},{30,31,32,41},{24,25,26,33,42},{34},{35},{36}};
D d77=new D(76,b77);
int[][] b78={{66,76,88},{65,75,87},{64,74,86},{63,73,85},{56,62,72,84,96},{51,55,61,71,83,95},{18,50,54,60},{44},{39,40,45},{30,31,32,41},{24,25,26,33,42},{34},{35},{36}};
D d78=new D(77,b78);
int[][] b79={{71,72,84,95},{60,61,62,73,85,96},{54,55,56,63,74,86},{50,51,64,75,87},{18,65,76,88},{44,66,77},{39,40,45},{30,31,32,41},{24,25,26,33,42},{34},{35},{36}};
D d79=new D(83,b79);
int[][] b80={{72,73,83,85,95,96},{61,62,63,71,74,86},{54,55,56,60,64,75,87},{50,51,65,76,88},{18,66,77},{44},{39,40,45},{30,31,32,41},{24,25,26,33,42},{34},{35},{36}};
D d80=new D(84,b80);
int[][] b81={{73,74,84,86,96},{62,63,64,72,75,83,87,95},{55,56,61,65,71,76,88},{50,51,54,60,66,77},{18},{44},{39,40,45},{30,31,32,41},{24,25,26,33,42},{34},{35},{36}};
D d81=new D(85,b81);
int[][] b82={{74,75,85,87},{63,64,65,73,76,84,88,96},{56,62,66,72,77,83,95},{51,55,61,71},{18,50,54,60},{44},{39,40,45},{30,31,32,41},{24,25,26,33,42},{34},{35},{36}};
D d82=new D(86,b82);
int[][] b83={{75,76,86,88},{64,65,66,74,77,85},{63,73,84,96},{56,62,72,83,95},{51,55,61,71},{18,50,54,60},{44},{39,40,45},{30,31,32,41},{24,25,26,33,42},{34},{35},{36}};
D d83=new D(87,b83);
int[][] b84={{76,77,87},{65,66,75,86},{64,74,85},{63,73,84,96},{56,62,72,83,95},{51,55,61,71},{18,50,54,60},{44},{39,40,45},{30,31,32,41},{24,25,26,33,42},{34},{35},{36}};
D d84=new D(88,b84);
int[][] b85={{83,84,96},{71,72,73,85},{60,61,62,63,74,86},{54,55,56,64,75,87},{50,51,65,76,88},{18,66,77},{44},{39,40,45},{30,31,32,41},{24,25,26,33,42},{34},{35},{36}};
D d85=new D(95,b85);
int[][] b86={{84,85,95},{72,73,74,83,86},{61,62,63,64,71,75,87},{54,55,56,60,65,76,88},{50,51,66,77},{18},{44},{39,40,45},{30,31,32,41},{24,25,26,33,42},{34},{35},{36}};
D d86=new D(96,b86);
D[] di3={d43,d44,d45,d46,d47,d48,d49,d50,d51,d52,d53,d54,d55,d56,d57,d58,d59,d60,d61,d62,d63,d64,d65,d66,d67,d68,d69,d70,d71,d72,d73,d74,d75,d76,d77,d78,d79,d80,d81,d82,d83,d84,d85,d86};
ListD listD4=new ListD(di3);
list.add(listD4) ;
int[][] b87={{53,59},{69,70},{68,79,80,81},{58,82,90,91,92,93},{94,98,99,100,101,102},{103,105,106,107,108,109,110},{111,114,115,116,117,118,119},{112,120,121,122,123,124,125},{126,128,129,130,131,132,133},{127,137},{134,141,142}};
D d87=new D(52,b87);
int[][] b88={{52,59},{69,70},{68,79,80,81},{58,82,90,91,92,93},{94,98,99,100,101,102},{103,105,106,107,108,109,110},{111,114,115,116,117,118,119},{112,120,121,122,123,124,125},{126,128,129,130,131,132,133},{127,137},{134,141,142}};
D d88=new D(53,b88);
int[][] b89={{68},{69,79},{59,70,80,90,91},{52,53,81,92,98,99,100},{82,93,101,105,106,107,108},{94,102,109,114,115,116,117},{103,110,118,120,121,122,123},{111,119,124,128,129,130,131,132},{112,125,133,137},{126,141,142},{127,148}};
D d89=new D(58,b89);
int[][] b90={{52,53,69,70},{68,79,80,81},{58,82,90,91,92,93},{94,98,99,100,101,102},{103,105,106,107,108,109,110},{111,114,115,116,117,118,119},{112,120,121,122,123,124,125},{126,128,129,130,131,132,133},{127,137},{134,141,142},{138,148}};
D d90=new D(59,b90);
int[][] b91={{58,69,79},{59,70,80,90,91},{52,53,81,92,98,99,100},{82,93,101,105,106,107,108},{94,102,109,114,115,116,117},{103,110,118,120,121,122,123},{111,119,124,128,129,130,131,132},{112,125,133,137},{126,141,142},{127,148},{134}};
D d91=new D(68,b91);
int[][] b92={{59,68,70,79,80},{52,53,58,81,90,91,92},{82,93,98,99,100,101},{94,102,105,106,107,108,109},{103,110,114,115,116,117,118},{111,119,120,121,122,123,124},{112,125,128,129,130,131,132,133},{126,137},{127,141,142},{134,148},{138}};
D d92=new D(69,b92);
int[][] b93={{59,69,80,81},{52,53,68,79,82,91,92,93},{58,90,94,99,100,101,102},{98,103,106,107,108,109,110},{105,111,114,115,116,117,118,119},{112,120,121,122,123,124,125},{126,128,129,130,131,132,133},{127,137},{134,141,142},{138,148},{139,144}};
D d93=new D(70,b93);
int[][] b94={{68,69,80,90,91},{58,59,70,81,92,98,99,100},{52,53,82,93,101,105,106,107,108},{94,102,109,114,115,116,117},{103,110,118,120,121,122,123},{111,119,124,128,129,130,131,132},{112,125,133,137},{126,141,142},{127,148},{134},{138}};
D d94=new D(79,b94);
int[][] b95={{69,70,79,81,91,92},{59,68,82,90,93,99,100,101},{52,53,58,94,98,102,106,107,108,109},{103,105,110,114,115,116,117,118},{111,119,120,121,122,123,124},{112,125,128,129,130,131,132,133},{126,137},{127,141,142},{134,148},{138},{139,144}};
D d95=new D(80,b95);
int[][] b96={{70,80,82,92,93},{59,69,79,91,94,100,101,102},{52,53,68,90,99,103,107,108,109,110},{58,98,106,111,115,116,117,118,119},{105,112,114,120,121,122,123,124,125},{126,128,129,130,131,132,133},{127,137},{134,141,142},{138,148},{139,144},{135,140,145,151}};
D d96=new D(81,b96);
int[][] b97={{81,93,94},{70,80,92,101,102,103},{59,69,79,91,100,108,109,110,111},{52,53,68,90,99,107,112,116,117,118,119},{58,98,106,115,121,122,123,124,125},{105,114,120,126,129,130,131,132,133},{127,128,137},{134,141,142},{138,148},{139,144},{135,140,145,151}};
D d97=new D(82,b97);
int[][] b98={{79,91,98,99},{68,69,80,92,100,105,106,107},{58,59,70,81,93,101,108,114,115,116},{52,53,82,94,102,109,117,120,121,122},{103,110,118,123,128,129,130,131},{111,119,124,132,137},{112,125,133,141,142},{126,148},{127},{134},{138}};
D d98=new D(90,b98);
int[][] b99={{79,80,90,92,99,100},{68,69,70,81,93,98,101,106,107,108},{58,59,82,94,102,105,109,114,115,116,117},{52,53,103,110,118,120,121,122,123},{111,119,124,128,129,130,131,132},{112,125,133,137},{126,141,142},{127,148},{134},{138},{139,144}};
D d99=new D(91,b99);
int[][] b100={{80,81,91,93,100,101},{69,70,79,82,90,94,99,102,107,108,109},{59,68,98,103,106,110,115,116,117,118},{52,53,58,105,111,114,119,120,121,122,123,124},{112,125,128,129,130,131,132,133},{126,137},{127,141,142},{134,148},{138},{139,144},{135,140,145,151}};
D d100=new D(92,b100);
int[][] b101={{81,82,92,94,101,102},{70,80,91,100,103,108,109,110},{59,69,79,90,99,107,111,116,117,118,119},{52,53,68,98,106,112,115,121,122,123,124,125},{58,105,114,120,126,129,130,131,132,133},{127,128,137},{134,141,142},{138,148},{139,144},{135,140,145,151},{136,146}};
D d101=new D(93,b101);
int[][] b102={{82,93,102,103},{81,92,101,109,110,111},{70,80,91,100,108,112,117,118,119},{59,69,79,90,99,107,116,122,123,124,125},{52,53,68,98,106,115,121,126,130,131,132,133},{58,105,114,120,127,129},{128,134,137},{138,141,142},{139,144,148},{135,140,145,151},{136,146}};
D d102=new D(94,b102);
int[][] b103={{90,99,105,106},{79,91,100,107,114,115},{68,69,80,92,101,108,116,120,121},{58,59,70,81,93,102,109,117,122,128,129,130},{52,53,82,94,103,110,118,123,131,137},{111,119,124,132,141,142},{112,125,133,148},{126},{127},{134},{138}};
D d103=new D(98,b103);
int[][] b104={{90,91,98,100,106,107},{79,80,92,101,105,108,114,115,116},{68,69,70,81,93,102,109,117,120,121,122},{58,59,82,94,103,110,118,123,128,129,130,131},{52,53,111,119,124,132,137},{112,125,133,141,142},{126,148},{127},{134},{138},{139,144}};
D d104=new D(99,b104);
int[][] b105={{91,92,99,101,107,108},{79,80,81,90,93,98,102,106,109,115,116,117},{68,69,70,82,94,103,105,110,114,118,120,121,122,123},{58,59,111,119,124,128,129,130,131,132},{52,53,112,125,133,137},{126,141,142},{127,148},{134},{138},{139,144},{135,140,145,151}};
D d105=new D(100,b105);
int[][] b106={{92,93,100,102,108,109},{80,81,82,91,94,99,103,107,110,116,117,118},{69,70,79,90,98,106,111,115,119,121,122,123,124},{59,68,105,112,114,120,125,129,130,131,132,133},{52,53,58,126,128,137},{127,141,142},{134,148},{138},{139,144},{135,140,145,151},{136,146}};
D d106=new D(101,b106);
int[][] b107={{93,94,101,103,109,110},{81,82,92,100,108,111,117,118,119},{70,80,91,99,107,112,116,122,123,124,125},{59,69,79,90,98,106,115,121,126,130,131,132,133},{52,53,68,105,114,120,127,129},{58,128,134,137},{138,141,142},{139,144,148},{135,140,145,151},{136,146},{147}};
D d107=new D(102,b107);
int[][] b108={{94,102,110,111},{82,93,101,109,112,118,119},{81,92,100,108,117,123,124,125},{70,80,91,99,107,116,122,126,131,132,133},{59,69,79,90,98,106,115,121,127,130},{52,53,68,105,114,120,129,134},{58,128,137,138},{139,141,142,144},{135,140,145,148,151},{136,146},{147}};
D d108=new D(103,b108);
int[][] b109={{98,106,114},{90,99,107,115,120},{79,91,100,108,116,121,128,129},{68,69,80,92,101,109,117,122,130,137},{58,59,70,81,93,102,110,118,123,131,141,142},{52,53,82,94,103,111,119,124,132,148},{112,125,133},{126},{127},{134},{138}};
D d109=new D(105,b109);
int[][] b110={{98,99,105,107,114,115},{90,91,100,108,116,120,121},{79,80,92,101,109,117,122,128,129,130},{68,69,70,81,93,102,110,118,123,131,137},{58,59,82,94,103,111,119,124,132,141,142},{52,53,112,125,133,148},{126},{127},{134},{138},{139,144}};
D d110=new D(106,b110);
int[][] b111={{99,100,106,108,115,116},{90,91,92,98,101,105,109,114,117,120,121,122},{79,80,81,93,102,110,118,123,128,129,130,131},{68,69,70,82,94,103,111,119,124,132,137},{58,59,112,125,133,141,142},{52,53,126,148},{127},{134},{138},{139,144},{135,140,145,151}};
D d111=new D(107,b111);
int[][] b112={{100,101,107,109,116,117},{91,92,93,99,102,106,110,115,118,121,122,123},{79,80,81,82,90,94,98,103,105,111,114,119,120,124,129,130,131,132},{68,69,70,112,125,128,133,137},{58,59,126,141,142},{52,53,127,148},{134},{138},{139,144},{135,140,145,151},{136,146}};
D d112=new D(108,b112);
int[][] b113={{101,102,108,110,117,118},{92,93,94,100,103,107,111,116,119,122,123,124},{80,81,82,91,99,106,112,115,121,125,130,131,132,133},{69,70,79,90,98,105,114,120,126,129},{59,68,127,128,137},{52,53,58,134,141,142},{138,148},{139,144},{135,140,145,151},{136,146},{147}};
D d113=new D(109,b113);
int[][] b114={{102,103,109,111,118,119},{93,94,101,108,112,117,123,124,125},{81,82,92,100,107,116,122,126,131,132,133},{70,80,91,99,106,115,121,127,130},{59,69,79,90,98,105,114,120,129,134},{52,53,68,128,137,138},{58,139,141,142,144},{135,140,145,148,151},{136,146},{147},{152}};
D d114=new D(110,b114);
int[][] b115={{103,110,112,119},{94,102,109,118,124,125},{82,93,101,108,117,123,126,132,133},{81,92,100,107,116,122,127,131},{70,80,91,99,106,115,121,130,134},{59,69,79,90,98,105,114,120,129,138},{52,53,68,128,137,139,144},{58,135,140,141,142,145,151},{136,146,148},{147},{152}};
D d115=new D(111,b115);
int[][] b116={{111},{103,110,119},{94,102,109,118,124,125},{82,93,101,108,117,123,126,132,133},{81,92,100,107,116,122,127,131},{70,80,91,99,106,115,121,130,134},{59,69,79,90,98,105,114,120,129,138},{52,53,68,128,137,139,144},{58,135,140,141,142,145,151},{136,146,148},{147}};
D d116=new D(112,b116);
int[][] b117={{105,106,115,120},{98,99,107,116,121,128,129},{90,91,100,108,117,122,130,137},{79,80,92,101,109,118,123,131,141,142},{68,69,70,81,93,102,110,119,124,132,148},{58,59,82,94,103,111,125,133},{52,53,112,126},{127},{134},{138},{139,144}};
D d117=new D(114,b117);
int[][] b118={{106,107,114,116,120,121},{98,99,100,105,108,117,122,128,129,130},{90,91,92,101,109,118,123,131,137},{79,80,81,93,102,110,119,124,132,141,142},{68,69,70,82,94,103,111,125,133,148},{58,59,112,126},{52,53,127},{134},{138},{139,144},{135,140,145,151}};
D d118=new D(115,b118);
int[][] b119={{107,108,115,117,121,122},{99,100,101,106,109,114,118,120,123,129,130,131},{90,91,92,93,98,102,105,110,119,124,128,132,137},{79,80,81,82,94,103,111,125,133,141,142},{68,69,70,112,126,148},{58,59,127},{52,53,134},{138},{139,144},{135,140,145,151},{136,146}};
D d119=new D(116,b119);
int[][] b120={{108,109,116,118,122,123},{100,101,102,107,110,115,119,121,124,130,131,132},{91,92,93,94,99,103,106,111,114,120,125,129,133},{79,80,81,82,90,98,105,112,126,128,137},{68,69,70,127,141,142},{58,59,134,148},{52,53,138},{139,144},{135,140,145,151},{136,146},{147}};
D d120=new D(117,b120);
int[][] b121={{109,110,117,119,123,124},{101,102,103,108,111,116,122,125,131,132,133},{92,93,94,100,107,112,115,121,126,130},{80,81,82,91,99,106,114,120,127,129},{69,70,79,90,98,105,128,134,137},{59,68,138,141,142},{52,53,58,139,144,148},{135,140,145,151},{136,146},{147},{152}};
D d121=new D(118,b121);
int[][] b122={{110,111,118,124,125},{102,103,109,112,117,123,126,132,133},{93,94,101,108,116,122,127,131},{81,82,92,100,107,115,121,130,134},{70,80,91,99,106,114,120,129,138},{59,69,79,90,98,105,128,137,139,144},{52,53,68,135,140,141,142,145,151},{58,136,146,148},{147},{152},{153}};
D d122=new D(119,b122);
int[][] b123={{114,115,121,128,129},{105,106,107,116,122,130,137},{98,99,100,108,117,123,131,141,142},{90,91,92,101,109,118,124,132,148},{79,80,81,93,102,110,119,125,133},{68,69,70,82,94,103,111,126},{58,59,112,127},{52,53,134},{138},{139,144},{135,140,145,151}};
D d123=new D(120,b123);
int[][] b124={{115,116,120,122,129,130},{106,107,108,114,117,123,128,131,137},{98,99,100,101,105,109,118,124,132,141,142},{90,91,92,93,102,110,119,125,133,148},{79,80,81,82,94,103,111,126},{68,69,70,112,127},{58,59,134},{52,53,138},{139,144},{135,140,145,151},{136,146}};
D d124=new D(121,b124);
int[][] b125={{116,117,121,123,130,131},{107,108,109,115,118,120,124,129,132},{99,100,101,102,106,110,114,119,125,128,133,137},{90,91,92,93,94,98,103,105,111,126,141,142},{79,80,81,82,112,127,148},{68,69,70,134},{58,59,138},{52,53,139,144},{135,140,145,151},{136,146},{147}};
D d125=new D(122,b125);
int[][] b126={{117,118,122,124,131,132},{108,109,110,116,119,121,125,130,133},{100,101,102,103,107,111,115,120,126,129},{91,92,93,94,99,106,112,114,127,128,137},{79,80,81,82,90,98,105,134,141,142},{68,69,70,138,148},{58,59,139,144},{52,53,135,140,145,151},{136,146},{147},{152}};
D d126=new D(123,b126);
int[][] b127={{118,119,123,125,132,133},{109,110,111,117,122,126,131},{101,102,103,108,112,116,121,127,130},{92,93,94,100,107,115,120,129,134},{80,81,82,91,99,106,114,128,137,138},{69,70,79,90,98,105,139,141,142,144},{59,68,135,140,145,148,151},{52,53,58,136,146},{147},{152},{153}};
D d127=new D(124,b127);
int[][] b128={{119,124,126,133},{110,111,118,123,127,132},{102,103,109,112,117,122,131,134},{93,94,101,108,116,121,130,138},{81,82,92,100,107,115,120,129,139,144},{70,80,91,99,106,114,128,135,137,140,145,151},{59,69,79,90,98,105,136,141,142,146},{52,53,68,147,148},{58,152},{153}};
D d128=new D(125,b128);
int[][] b129={{125,127},{119,124,133,134},{110,111,118,123,132,138},{102,103,109,112,117,122,131,139,144},{93,94,101,108,116,121,130,135,140,145,151},{81,82,92,100,107,115,120,129,136,146},{70,80,91,99,106,114,128,137,147},{59,69,79,90,98,105,141,142,152},{52,53,68,148,153},{58}};
D d129=new D(126,b129);
int[][] b130={{126,134},{125,138},{119,124,133,139,144},{110,111,118,123,132,135,140,145,151},{102,103,109,112,117,122,131,136,146},{93,94,101,108,116,121,130,147},{81,82,92,100,107,115,120,129,152},{70,80,91,99,106,114,128,137,153},{59,69,79,90,98,105,141,142},{52,53,68,148},{58}};
D d130=new D(127,b130);
int[][] b131={{120,129,137},{114,115,121,130,141,142},{105,106,107,116,122,131,148},{98,99,100,108,117,123,132},{90,91,92,101,109,118,124,133},{79,80,81,93,102,110,119,125},{68,69,70,82,94,103,111,126},{58,59,112,127},{52,53,134},{138},{139,144}};
D d131=new D(128,b131);
int[][] b132={{120,121,128,130,137},{114,115,116,122,131,141,142},{105,106,107,108,117,123,132,148},{98,99,100,101,109,118,124,133},{90,91,92,93,102,110,119,125},{79,80,81,82,94,103,111,126},{68,69,70,112,127},{58,59,134},{52,53,138},{139,144},{135,140,145,151}};
D d132=new D(129,b132);
int[][] b133={{121,122,129,131},{115,116,117,120,123,128,132,137},{106,107,108,109,114,118,124,133,141,142},{98,99,100,101,102,105,110,119,125,148},{90,91,92,93,94,103,111,126},{79,80,81,82,112,127},{68,69,70,134},{58,59,138},{52,53,139,144},{135,140,145,151},{136,146}};
D d133=new D(130,b133);
int[][] b134={{122,123,130,132},{116,117,118,121,124,129,133},{107,108,109,110,115,119,120,125,128,137},{99,100,101,102,103,106,111,114,126,141,142},{90,91,92,93,94,98,105,112,127,148},{79,80,81,82,134},{68,69,70,138},{58,59,139,144},{52,53,135,140,145,151},{136,146},{147}};
D d134=new D(131,b134);
int[][] b135={{123,124,131,133},{117,118,119,122,125,130},{108,109,110,111,116,121,126,129},{100,101,102,103,107,112,115,120,127,128,137},{91,92,93,94,99,106,114,134,141,142},{79,80,81,82,90,98,105,138,148},{68,69,70,139,144},{58,59,135,140,145,151},{52,53,136,146},{147},{152}};
D d135=new D(132,b135);
int[][] b136={{124,125,132},{118,119,123,126,131},{109,110,111,117,122,127,130},{101,102,103,108,112,116,121,129,134},{92,93,94,100,107,115,120,128,137,138},{80,81,82,91,99,106,114,139,141,142,144},{69,70,79,90,98,105,135,140,145,148,151},{59,68,136,146},{52,53,58,147},{152},{153}};
D d136=new D(133,b136);
int[][] b137={{127,138},{126,139,144},{125,135,140,145,151},{119,124,133,136,146},{110,111,118,123,132,147},{102,103,109,112,117,122,131,152},{93,94,101,108,116,121,130,153},{81,82,92,100,107,115,120,129},{70,80,91,99,106,114,128,137},{59,69,79,90,98,105,141,142},{52,53,68,148}};
D d137=new D(134,b137);
int[][] b138={{136,139,140},{138,145,146},{134,144,147},{127,151,152},{126,153},{125},{119,124,133},{110,111,118,123,132},{102,103,109,112,117,122,131},{93,94,101,108,116,121,130},{81,82,92,100,107,115,120,129}};
D d138=new D(135,b138);
int[][] b139={{135,140},{139,145,146},{138,147},{134,144,152},{127,151,153},{126},{125},{119,124,133},{110,111,118,123,132},{102,103,109,112,117,122,131},{93,94,101,108,116,121,130}};
D d139=new D(136,b139);
int[][] b140={{128,129,141,142},{120,121,130,148},{114,115,116,122,131},{105,106,107,108,117,123,132},{98,99,100,101,109,118,124,133},{90,91,92,93,102,110,119,125},{79,80,81,82,94,103,111,126},{68,69,70,112,127},{58,59,134},{52,53,138},{139,144}};
D d140=new D(137,b140);
int[][] b141={{134,139,144},{127,135,140,145,151},{126,136,146},{125,147},{119,124,133,152},{110,111,118,123,132,153},{102,103,109,112,117,122,131},{93,94,101,108,116,121,130},{81,82,92,100,107,115,120,129},{70,80,91,99,106,114,128,137},{59,69,79,90,98,105,141,142}};
D d141=new D(138,b141);
int[][] b142={{135,138,140,145},{134,136,144,146},{127,147,151},{126,152},{125,153},{119,124,133},{110,111,118,123,132},{102,103,109,112,117,122,131},{93,94,101,108,116,121,130},{81,82,92,100,107,115,120,129},{70,80,91,99,106,114,128,137}};
D d142=new D(139,b142);
int[][] b143={{135,136,139,145,146},{138,147},{134,144,152},{127,151,153},{126},{125},{119,124,133},{110,111,118,123,132},{102,103,109,112,117,122,131},{93,94,101,108,116,121,130},{81,82,92,100,107,115,120,129}};
D d143=new D(140,b143);
int[][] b144={{137,142,148},{128,129},{120,121,130},{114,115,116,122,131},{105,106,107,108,117,123,132},{98,99,100,101,109,118,124,133},{90,91,92,93,102,110,119,125},{79,80,81,82,94,103,111,126},{68,69,70,112,127},{58,59,134},{52,53,138}};
D d144=new D(141,b144);
int[][] b145={{137,141,148},{128,129},{120,121,130},{114,115,116,122,131},{105,106,107,108,117,123,132},{98,99,100,101,109,118,124,133},{90,91,92,93,102,110,119,125},{79,80,81,82,94,103,111,126},{68,69,70,112,127},{58,59,134},{52,53,138}};
D d145=new D(142,b145);
int[][] b146={{138,151},{134,139},{127,135,140,145},{126,136,146},{125,147},{119,124,133,152},{110,111,118,123,132,153},{102,103,109,112,117,122,131},{93,94,101,108,116,121,130},{81,82,92,100,107,115,120,129},{70,80,91,99,106,114,128,137}};
D d146=new D(144,b146);
int[][] b147={{139,140,146},{135,136,138,147},{134,144,152},{127,151,153},{126},{125},{119,124,133},{110,111,118,123,132},{102,103,109,112,117,122,131},{93,94,101,108,116,121,130},{81,82,92,100,107,115,120,129}};
D d147=new D(145,b147);
int[][] b148={{140,145,147},{135,136,139,152},{138,153},{134,144},{127,151},{126},{125},{119,124,133},{110,111,118,123,132},{102,103,109,112,117,122,131},{93,94,101,108,116,121,130}};
D d148=new D(146,b148);
int[][] b149={{146,152},{140,145,153},{135,136,139},{138},{134,144},{127,151},{126},{125},{119,124,133},{110,111,118,123,132},{102,103,109,112,117,122,131}};
D d149=new D(147,b149);
int[][] b150={{141,142},{137},{128,129},{120,121,130},{114,115,116,122,131},{105,106,107,108,117,123,132},{98,99,100,101,109,118,124,133},{90,91,92,93,102,110,119,125},{79,80,81,82,94,103,111,126},{68,69,70,112,127},{58,59,134}};
D d150=new D(148,b150);
int[][] b151={{144},{138},{134,139},{127,135,140,145},{126,136,146},{125,147},{119,124,133,152},{110,111,118,123,132,153},{102,103,109,112,117,122,131},{93,94,101,108,116,121,130},{81,82,92,100,107,115,120,129}};
D d151=new D(151,b151);
int[][] b152={{147,153},{146},{140,145},{135,136,139},{138},{134,144},{127,151},{126},{125},{119,124,133},{110,111,118,123,132}};
D d152=new D(152,b152);
int[][] b153={{152},{147},{146},{140,145},{135,136,139},{138},{134,144},{127,151},{126},{125},{119,124,133}};
D d153=new D(153,b153);
D[] di4={d87,d88,d89,d90,d91,d92,d93,d94,d95,d96,d97,d98,d99,d100,d101,d102,d103,d104,d105,d106,d107,d108,d109,d110,d111,d112,d113,d114,d115,d116,d117,d118,d119,d120,d121,d122,d123,d124,d125,d126,d127,d128,d129,d130,d131,d132,d133,d134,d135,d136,d137,d138,d139,d140,d141,d142,d143,d144,d145,d146,d147,d148,d149,d150,d151,d152,d153};
ListD listD5=new ListD(di4);
list.add(listD5) ;
return list;
}
}
