package fr.montpellier.supperform.excel;

import fr.montpellier.supperform.affichage.FenetreAlert;
import fr.montpellier.supperform.resolution.Calcul;
import org.apache.poi.openxml4j.exceptions.ODFNotOfficeXmlFileException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;

public class TableauExcel {

    public XSSFWorkbook recuperationFichier(File file) {
        try {
            FileInputStream fileInputStreamReponse = new FileInputStream(file);
            return new XSSFWorkbook(fileInputStreamReponse);
        } catch (NullPointerException n) {
            FenetreAlert.erreur("Le fichier n'a pas était trouvé. \nVeuillez réessayer");
        } catch (ODFNotOfficeXmlFileException n){
            FenetreAlert.erreur("Le format du fichier est incorect. \nVeuillez réessayer");
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int verifValueInt(int nb, String text){
        if( nb != 0){
            return nb;
        }else {
            FenetreAlert.erreur(text);
            return 0;
        }
    }

    public String[][] recuperationValeurIdentifiant(int nbLigneEtuId, XSSFSheet sheet){

        String[][] idEtu = new String[4][nbLigneEtuId];

        for (int i = 0; i < nbLigneEtuId; i++) {

            XSSFCell cellIdPrenom = sheet.getRow(i + 1).getCell(1);
            XSSFCell cellIdNom = sheet.getRow(i + 1).getCell(0);

            XSSFCell cellId = sheet.getRow(i + 1).getCell(2);
            XSSFCell cellIdLieuInscription = sheet.getRow(i + 1).getCell(3);

            idEtu[0][i] = cellIdPrenom.getStringCellValue().toUpperCase();
            idEtu[1][i] = cellIdNom.getStringCellValue().toUpperCase();
            idEtu[2][i] = cellId.toString();
            idEtu[3][i] = cellIdLieuInscription.toString();

        }
        return idEtu;
    }

    public void attributionIdentifiant(int nombreLigne, XSSFSheet sheet, String[][] idEtu){
        XSSFCell cellReponseIdentifiant = sheet.getRow(0).getCell(2);
        cellReponseIdentifiant.setCellValue("Identifiant contrôle");

        XSSFCell cellReponseLieu = sheet.getRow(0).getCell(3);
        cellReponseLieu.setCellValue("Lieu d'inscription");

        for (int i = 0; i < nombreLigne; i++) {

            XSSFCell cellReponseNom = sheet.getRow(i + 1).getCell(0);
            String reponseNom = cellReponseNom.getStringCellValue().toUpperCase();

            XSSFCell cellReponsePrenom = sheet.getRow(i + 1).getCell(1);
            String reponsePrenom = cellReponsePrenom.getStringCellValue().toUpperCase();

            XSSFCell cellReponseId = sheet.getRow(i + 1).createCell(2);
            cellReponseId.setCellValue("");

            XSSFCell cellReponseLieuInscription = sheet.getRow(i + 1).createCell(3);
            cellReponseLieuInscription.setCellValue("");

            if(idEtu[0][i].contains(reponsePrenom) && idEtu[1][i].contains(reponseNom)){
                cellReponseId.setCellValue(idEtu[2][i]);
                cellReponseLieuInscription.setCellValue(idEtu[3][i]);
            }
        }
    }

    public void calculNote(int ligneRep, int nombreLigne, int nombreColonne, XSSFSheet sheetReponse, double retrait){
        if(ligneRep > nombreLigne){

            int positionDebutReponse = 0;
            while (!sheetReponse.getRow(0).getCell(positionDebutReponse).getStringCellValue().startsWith("Réponse 1")){
                positionDebutReponse++;
            }

            sheetReponse.getRow(0).createCell(positionDebutReponse + nombreColonne + 4 + nombreColonne).setCellValue("Note finale");

            Calcul calcul = new Calcul(retrait);

            for (int i = 0; i < nombreColonne; i++) {

                XSSFCell cellReponseQCM = sheetReponse.getRow(ligneRep - 1).getCell(i);
                ArrayList<String> reponseQCM = calcul.creationTableauReponse(cellReponseQCM);

                sheetReponse.getRow(0).createCell(positionDebutReponse + i + nombreColonne + 3).setCellValue("Note réponse " + (i+1));

                for (int j = 0; j < nombreLigne; j++) {      //génère les résultats de chaque QCM pour chaque Etudiant et l'ajoute dans un tableau à deux dimensions

                    XSSFCell cellReponseEtudiantQCM = sheetReponse.getRow(1 + j).getCell(positionDebutReponse + i);
                    ArrayList<String> reponseEtudiantQCM = calcul.creationTableau(cellReponseEtudiantQCM);

                    double resultat = calcul.resultatEtudiantAuQCM(reponseQCM, reponseEtudiantQCM);      //calcul du resultat en appelant la fonction resultatEtudiantAuQCM

                    XSSFCell cellResultatEtudiantQCM = sheetReponse.getRow(1 + j).createCell(positionDebutReponse + i + nombreColonne + 3); //créé les nouvelles cellules où seront enregistrées les résultats
                    cellResultatEtudiantQCM.setCellValue("");

                    cellResultatEtudiantQCM.setCellValue(resultat);    //rempli la cellule avec les résultats

                    if(sheetReponse.getRow(1 + j).getCell(positionDebutReponse + nombreColonne + 4 + nombreColonne) == null){
                        XSSFCell cellResultatTotalEtudiantQCM = sheetReponse.getRow(1 + j).createCell(positionDebutReponse + nombreColonne + 4 + nombreColonne);
                        cellResultatTotalEtudiantQCM.setCellValue(cellResultatTotalEtudiantQCM.getNumericCellValue() + resultat);
                    }else {
                        XSSFCell cellResultatTotalEtudiantQCM = sheetReponse.getRow(1 + j).getCell(positionDebutReponse + nombreColonne + 4 + nombreColonne);
                        cellResultatTotalEtudiantQCM.setCellValue(cellResultatTotalEtudiantQCM.getNumericCellValue() + resultat);
                    }
                }
            }
        }else if(ligneRep == 0){
            FenetreAlert.erreur("Vous ne pouvez pas entrer les réponses à la ligne 0. \nVeuillez réessayer");
        }else {
            FenetreAlert.erreur("La ligne à laquelle vous avez entré les bonnes réponses doit être supérieur au nombre d'étudiant. \nVeuillez réessayer");
        }
    }

    public void fermetureFichier(XSSFWorkbook workbook, File file) {
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            workbook.close();
            fileOut.close();
            FenetreAlert.info("Le programme a correctement fonctionné !");
        } catch (FileNotFoundException | NullPointerException n) {
            FenetreAlert.erreur("Fermer les fichiers Excel puis recommencer.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
