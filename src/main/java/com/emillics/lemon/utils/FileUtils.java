package com.emillics.lemon.utils;

import com.drew.imaging.mp4.Mp4MetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.emillics.lemon.model.Result;
import com.emillics.lemon.model.Video;
import io.reactivex.rxjava3.annotations.NonNull;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileUtils {

    public static byte[] getBytes(@NonNull String filename) throws IOException {
        return getBytes(new File(filename));
    }

    /**
     * NIO way
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] getBytes(@NonNull File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }

        FileChannel channel = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(file);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
                // System.out.println("reading");
            }
            return byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static File saveFile(Window window, String fileType, String fileExtension, String fileName) {
        FileChooser fileChooser = new FileChooser();
        if (fileName != null && !fileName.isEmpty()) fileChooser.setInitialFileName(fileName);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(fileType, fileExtension);
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser.showSaveDialog(window);
    }

//    public static Result<Boolean> exportExcel(File file, List<String[]> data) {
//        String sheetName = file.getName().substring(0, file.getName().indexOf("."));
//        HSSFWorkbook workbook = new HSSFWorkbook();// 创建一个Excel文件
//
//        // 设置标题字体
//        HSSFFont titleFont = workbook.createFont();
//        titleFont.setFontName("黑体");
//        titleFont.setFontHeightInPoints((short) 15);// 字体大小
//        titleFont.setBold(true);// 加粗
//
//        //标题单元格样式
//        HSSFCellStyle titleStyle = workbook.createCellStyle();
//        titleStyle.setFont(titleFont);
//        titleStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
//        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
////        titleStyle.setWrapText(true);// 自动换行
//
//        // 设置列头字体
//        HSSFFont headFont = workbook.createFont();
//        headFont.setFontName("黑体");
//        headFont.setFontHeightInPoints((short) 12);// 字体大小
//        headFont.setBold(true);// 加粗
//
//        // 列头样式
//        HSSFCellStyle columnHeadStyle = workbook.createCellStyle();
//        columnHeadStyle.setFont(headFont);
//        columnHeadStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
//        columnHeadStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
////        columnHeadStyle.setWrapText(true);// 自动换行
//
//        // cell样式
//        HSSFCellStyle cellStyle = workbook.createCellStyle();
//        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
////        cellStyle.setWrapText(true);// 自动换行
//
//        HSSFRow row;
//        HSSFCell cell;
//
//        String[] columnNames = data.remove(0);
//        int sheets = data.size() / 65533 + (data.size() % 65533 == 0 ? 0 : 1);
//
//        for (int page = 0; page < sheets; page++) {
//            String pageTitle = sheets == 1 ? sheetName : (sheetName + " - " + (page + 1));
//            HSSFSheet sheet = workbook.createSheet(pageTitle);
//
//            //列宽
//            for (int c = 0; c < data.get(0).length; c++) {
//                sheet.setColumnWidth(c, c == 0 ? 2000 : 5000);
//            }
//
//            //第一行(标题)
//            row = sheet.createRow(0);
//            row.setHeight((short) 600);
//            cell = row.createCell(0);
//            cell.setCellStyle(titleStyle);
//            cell.setCellValue(new HSSFRichTextString(pageTitle));
//            CellRangeAddress range0 = new CellRangeAddress(0, 0, 0, data.get(0).length - 1);
//            sheet.addMergedRegion(range0);
//
//            //第二行(列名)
//            row = sheet.createRow(1);
//            for (int j = 0; j < columnNames.length; j++) {
//                cell = row.createCell(j);
//                cell.setCellStyle(columnHeadStyle);
//                cell.setCellValue(new HSSFRichTextString(columnNames[j]));
//            }
//
//            //第三行开始(数据)
//            int itemIndex = 65533 * page;
//            for (int i = 0; i < (page == sheets - 1 ? (data.size() - 65533 * page) : 65533); i++) {
//                row = sheet.createRow(i + 2);
//                for (int j = 0; j < columnNames.length; j++) {
//                    cell = row.createCell(j);
//                    cell.setCellStyle(cellStyle);
//                    cell.setCellValue(new HSSFRichTextString(data.get(itemIndex + i)[j]));
//                }
//            }
//        }
//
//        boolean success = true;
//        String error = null;
//        try {
//            FileOutputStream ofs = new FileOutputStream(file);
//            workbook.write(ofs);
//            ofs.flush();
//            ofs.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            success = false;
//            error = e.getMessage();
//        }
//        return new Result<>(success, error);
//    }

//    public static Result<Boolean> exportRiskAssessExcel(File file,
//                                                        List<String> apperColumnNames,
//                                                        List<String> riskAssessColumnNames,
//                                                        List<IApper> apperList,
//                                                        List<IApperRiskAssess> riskAssessList) {
//        String sheetName = file.getName().substring(0, file.getName().indexOf("."));
//        HSSFWorkbook workbook = new HSSFWorkbook();// 创建一个Excel文件
//
//        // 设置标题字体
//        HSSFFont titleFont = workbook.createFont();
//        titleFont.setFontName("黑体");
//        titleFont.setFontHeightInPoints((short) 15);// 字体大小
//        titleFont.setBold(true);// 加粗
//
//        //标题单元格样式
//        HSSFCellStyle titleStyle = workbook.createCellStyle();
//        titleStyle.setFont(titleFont);
//        titleStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
//        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
////        titleStyle.setWrapText(true);// 自动换行
//
//        // 设置列头字体
//        HSSFFont headFont = workbook.createFont();
//        headFont.setFontName("黑体");
//        headFont.setFontHeightInPoints((short) 12);// 字体大小
//        headFont.setBold(true);// 加粗
//
//        // 列头样式
//        HSSFCellStyle columnHeadStyle = workbook.createCellStyle();
//        columnHeadStyle.setFont(headFont);
//        columnHeadStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
//        columnHeadStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
////        columnHeadStyle.setWrapText(true);// 自动换行
//
//        // cell样式
//        HSSFCellStyle cellStyle = workbook.createCellStyle();
//        cellStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
//        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
////        cellStyle.setWrapText(true);// 自动换行
//
//        HSSFRow row;
//        HSSFCell cell;
//
//        String[] mainColumns = apperColumnNames.toArray(new String[0]);
//        String[] subColumns = riskAssessColumnNames.toArray(new String[0]);
//
//        HSSFSheet sheet = workbook.createSheet(sheetName);
//
//        //列宽
//        for (int c = 0; c < mainColumns.length; c++) {
//            sheet.setColumnWidth(c, c == 0 ? 2000 : 5000);
//        }
//
//        //第一行(标题)
//        row = sheet.createRow(0);
//        row.setHeight((short) 600);
//        cell = row.createCell(0);
//        cell.setCellStyle(titleStyle);
//        cell.setCellValue(new HSSFRichTextString(sheetName));
//        CellRangeAddress range0 = new CellRangeAddress(0, 0, 0, mainColumns.length - 1);
//        sheet.addMergedRegion(range0);
//
//        //循环每个人的数据
//        int no = 1, line = 1;
//        for (IApper apper : apperList) {
//            //个人信息(列名)
//            row = sheet.createRow(line);
//            for (int j = 0; j < mainColumns.length; j++) {
//                cell = row.createCell(j);
//                cell.setCellStyle(columnHeadStyle);
//                cell.setCellValue(new HSSFRichTextString(mainColumns[j]));
//            }
//            //个人信息(数据)
//            row = sheet.createRow(++line);
//            String[] data = {String.valueOf(no),
//                    apper.getApperName() == null ? "" : apper.getApperName(),
//                    apper.getDistrictName() == null ? "" : apper.getDistrictName(),
//                    apper.getStreetName() == null ? "" : apper.getStreetName(),
//                    apper.getSex() == null ? "" : apper.getSex(),
//                    apper.getIdCard() == null ? "" : apper.getIdCard(),
//                    apper.getTel() == null ? "" : apper.getTel(),
//                    apper.getRegulateLevel() == null ? "" : apper.getRegulateLevel() == 1 ? "重" : apper.getRegulateLevel() == 2 ? "严" : "普",
//                    apper.getRegulateDateStart() == null ? "" : apper.getRegulateDateStart().toString().subSequence(0, 10).toString(),
//                    apper.getRegulateDateEnd() == null ? "" : apper.getRegulateDateEnd().toString().subSequence(0, 10).toString(),
//                    apper.getCreateTime() == null ? "" : apper.getCreateTime().toString().subSequence(0, 10).toString(),
//                    apper.getUncorrectTime() == null ? "" : apper.getUncorrectTime().toString().subSequence(0, 10).toString(),
//                    apper.getStatus() ? "在矫" : "已解矫"};
//            for (int j = 0; j < mainColumns.length; j++) {
//                cell = row.createCell(j);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue(new HSSFRichTextString(data[j]));
//            }
//            //心理评估(列名)
//            row = sheet.createRow(++line);
//            for (int j = 0; j < subColumns.length; j++) {
//                cell = row.createCell(j);
//                cell.setCellStyle(columnHeadStyle);
//                cell.setCellValue(new HSSFRichTextString(subColumns[j]));
//            }
//            //心理评估(数据)
//            for (int i = riskAssessList.size() - 1; i >= 0; i--) {
//                IApperRiskAssess riskAssess = riskAssessList.get(i);
//                if (riskAssess.getApperId().equals(apper.getId())) {
//                    row = sheet.createRow(++line);
//                    String[] details = {"",
//                            riskAssess.getStage() == 1 ? "初期" : riskAssess.getStage() == 2 ? "中期" : "末期",
//                            riskAssess.getAssessStatus() == 0 ? "未开始" : riskAssess.getStage() == 1 ? "已完成" : "进行中",
//                            riskAssess.getClosedTime().toString().subSequence(0, 19).toString(),
//                            riskAssess.getAppEntrance(),
//                            riskAssess.getAssessLavel()};
//                    for (int j = 0; j < subColumns.length; j++) {
//                        cell = row.createCell(j);
//                        cell.setCellStyle(cellStyle);
//                        cell.setCellValue(new HSSFRichTextString(details[j]));
//                    }
//                    riskAssessList.remove(i);
//                }
//            }
//            //与下一个人之间空三行
//            sheet.createRow(++line);
//            sheet.createRow(++line);
//            sheet.createRow(++line);
//            line++;
//            no++;
//        }
//        boolean success = true;
//        String error = null;
//        try {
//            FileOutputStream ofs = new FileOutputStream(file);
//            workbook.write(ofs);
//            ofs.flush();
//            ofs.close();
//        } catch (
//                IOException e) {
//            e.printStackTrace();
//            success = false;
//            error = e.getMessage();
//        }
//        return new Result<>(success, error);
//    }

    //    public static Result<String> genVisitReport(IVisitRecord visitRecord, File toFile) {
//        String time = visitRecord.getVisitTime();
//        String name = visitRecord.getVisitor();
//
//        HSSFWorkbook workbook = new HSSFWorkbook();// 创建一个Excel文件
//        HSSFSheet sheet = workbook.createSheet();
//        sheet.setColumnWidth(0, 3200);
//        sheet.setColumnWidth(1, 6000);
//        sheet.setColumnWidth(2, 3000);
//        sheet.setColumnWidth(3, 6000);
//        sheet.setColumnWidth(4, 3000);
//        sheet.setColumnWidth(5, 6000);
//
//        // 设置字体
//        HSSFFont headFont = workbook.createFont();
//        headFont.setFontName("黑体");
////                headFont.setFontHeightInPoints((short) 22);// 字体大小
//        headFont.setBold(true);// 加粗
//        // 标题样式
//        HSSFCellStyle headStyle = workbook.createCellStyle();
//        headStyle.setFont(headFont);
////                headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
//        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
//        headStyle.setWrapText(true);// 自动换行
//        // cell样式
//        HSSFCellStyle cellStyle = workbook.createCellStyle();
//        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
//        cellStyle.setWrapText(true);// 自动换行
//
//        HSSFRow row;
//        HSSFCell cell;
//
//        //第一行
//        row = sheet.createRow(0);
//        row.setHeight((short) 700);
//        cell = row.createCell(0);
//        cell.setCellStyle(headStyle);
//        cell.setCellValue(new HSSFRichTextString("工作单位"));
//        cell = row.createCell(1);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue(new HSSFRichTextString(visitRecord.getWorkUnit()));
//        CellRangeAddress range0 = new CellRangeAddress(0, 0, 1, 5);
//        sheet.addMergedRegion(range0);
//
//        //第二行
//        row = sheet.createRow(1);
//        row.setHeight((short) 700);
//        cell = row.createCell(0);
//        cell.setCellStyle(headStyle);
//        cell.setCellValue(new HSSFRichTextString("被走访人员姓名"));
//        cell = row.createCell(1);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue(new HSSFRichTextString(name));
//        cell = row.createCell(2);
//        cell.setCellStyle(headStyle);
//        cell.setCellValue(new HSSFRichTextString("性别"));
//        row.createCell(3).setCellValue(new HSSFRichTextString(""));
//        cell = row.createCell(4);
//        cell.setCellStyle(headStyle);
//        cell.setCellValue(new HSSFRichTextString("刑罚执行类别"));
//        row.createCell(5).setCellValue(new HSSFRichTextString(""));
//
//        //第三行
//        row = sheet.createRow(2);
//        row.setHeight((short) 700);
//        cell = row.createCell(0);
//        cell.setCellStyle(headStyle);
//        cell.setCellValue(new HSSFRichTextString("走访时间"));
//        cell = row.createCell(1);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue(new HSSFRichTextString(time));
//        cell = row.createCell(2);
//        cell.setCellStyle(headStyle);
//        cell.setCellValue(new HSSFRichTextString("走访场所"));
//        cell = row.createCell(3);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue(new HSSFRichTextString(visitRecord.getPlace()));
//        cell = row.createCell(4);
//        cell.setCellStyle(headStyle);
//        cell.setCellValue(new HSSFRichTextString("工作人员"));
//        cell = row.createCell(5);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue(new HSSFRichTextString(visitRecord.getPersonnel()));
//
//        //第四行
//        row = sheet.createRow(3);
//        row.setHeight((short) 700);
//        cell = row.createCell(0);
//        cell.setCellStyle(headStyle);
//        cell.setCellValue(new HSSFRichTextString("走访事由"));
//        cell = row.createCell(1);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue(new HSSFRichTextString(visitRecord.getVisitReason()));
//        CellRangeAddress range1 = new CellRangeAddress(3, 3, 1, 5);
//        sheet.addMergedRegion(range1);
//
//        //第五行
//        row = sheet.createRow(4);
//        row.setHeightInPoints((short) 450);
//        cell = row.createCell(0);
//        cell.setCellStyle(headStyle);
//        cell.setCellValue(new HSSFRichTextString("走访情况记录"));
//        cell = row.createCell(1);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue(new HSSFRichTextString(visitRecord.getRemark()));
//        CellRangeAddress range2 = new CellRangeAddress(4, 6, 0, 0);
//        CellRangeAddress range3 = new CellRangeAddress(4, 6, 1, 5);
//        sheet.addMergedRegion(range2);
//        sheet.addMergedRegion(range3);
//
//        String fileName = toFile == null ? (System.getProperty("java.io.tmpdir") + File.separator + time.substring(0, time.indexOf(' ')) +
//                visitRecord.getStreetName() + " - " + name + ".xls") : toFile.getAbsolutePath();
//        String error = null;
//        try {
//            FileOutputStream ofs = new FileOutputStream(fileName);
//            workbook.write(ofs);
//            ofs.flush();
//            ofs.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//            fileName = null;
//        }
//        return new Result<>(fileName, error);
//    }
//
    public static Result<Boolean> downloadFile(String url, String filepath, IFileDownloadListener fileDownloadListener) {
        Boolean success = null;
        String error = null;
        byte[] buf = new byte[1024];
        HttpURLConnection connection = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            long fileSize = connection.getContentLengthLong();
            connection.connect();
            bis = new BufferedInputStream(connection.getInputStream());
            fos = new FileOutputStream(filepath);
            int size = 0, totalSize = 0;
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
                totalSize += size;
                if (fileDownloadListener != null)
                    fileDownloadListener.onDownloading(url, filepath, fileSize, totalSize);
            }
            fos.flush();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
            error = e.getMessage();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    error = e.getMessage();
                }
            }
            if (connection != null) connection.disconnect(); //HttpURLConnection断开连接
        }
        return new Result<>(success, error);
    }

    public interface IFileDownloadListener {
        void onDownloading(String url, String localFile, long total, long downloaded);
    }

    public static void getVideoMetaData(Video video) {
        File file = new File(video.getPath());
        try {
            Metadata metadata = Mp4MetadataReader.readMetadata(file);
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    if (tag.getTagName().equals("Major Brand")) {
                        video.setFormat(tag.getDescription().split(" ")[0]);
                    }
                    if (tag.getTagName().equals("File Size")) {
                        video.setSize(Long.parseLong(tag.getDescription().split(" ")[0]));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String parseFileSize(Long fileSize) {
        if (fileSize >= 1073741824) {
            return fileSize / 1024 / 1024 / 1024 + " GB";
        } else if (fileSize >= 1048576) {
            return fileSize / 1024 / 1024 + " MB";
        } else if (fileSize > 1024) {
            return fileSize / 1024 + " KB";
        }
        return fileSize + " B";
    }
}
