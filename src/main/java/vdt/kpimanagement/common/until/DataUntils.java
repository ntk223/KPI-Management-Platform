//package vdt.kpimanagement.common.until;
//
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.*;
//import org.springframework.http.client.SimpleClientHttpRequestFactory;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.math.RoundingMode;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.sql.Timestamp;
//import java.text.DateFormat;
//import java.text.DecimalFormat;
//import java.text.Normalizer;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.function.Function;
//import java.util.function.Predicate;
//import java.util.regex.Pattern;
//
//public class DataUtils {
//
//    private static Logger log = LoggerFactory.getLogger(DataUtils.class);
//
//    public static boolean isNullOrEmpty(final Collection<?> collection) {
//        return collection == null || collection.isEmpty();
//    }
//
//    public static boolean isNullOrEmpty(String[] array) {
//        return array == null || array.length == 0;
//    }
//
//    public static boolean isNullOrEmpty(Object o) {
//        return o == null;
//    }
//
//    public static boolean isNull(Object o) {
//        return o == null;
//    }
//
//    public static boolean isNullOrEmpty(String string) {
//        return string == null || string.trim().isEmpty();
//    }
//
//    public static BigDecimal safeToBigDecimal(Object obj) {
//        if (obj == null) {
//            return BigDecimal.ZERO;
//        }
//        if (obj instanceof BigDecimal bigDecimal) {
//            return bigDecimal;
//        }
//        if (obj instanceof BigInteger bigInteger) {
//            return new BigDecimal(bigInteger);
//        }
//        if (obj instanceof Integer || obj instanceof Long || obj instanceof Short || obj instanceof Byte) {
//            return BigDecimal.valueOf(((Number) obj).longValue());
//        }
//        if (obj instanceof Float || obj instanceof Double) {
//            return BigDecimal.valueOf(((Number) obj).doubleValue());
//        }
//        if (obj instanceof String str && !str.trim().isEmpty()) {
//            try {
//                return new BigDecimal(str.trim());
//            } catch (NumberFormatException e) {
//                return BigDecimal.ZERO;
//            }
//        }
//        return BigDecimal.ZERO;
//    }
//
//    public static String safeToString(Object obj) {
//        if (obj == null) {
//            return "";
//        }
//        return obj.toString();
//    }
//
//    public static String trimToNull(Object obj) {
//        if (obj == null) {
//            return null;
//        }
//
//        String value = obj.toString().trim();
//        return value.isEmpty() ? null : value;
//    }
//
//    public static String setNullIfEmptyString(Object obj) {
//        if (DataUtils.isNullOrEmpty(obj)) {
//            return null;
//        }
//        return obj.toString();
//    }
//
//    public static Character safeToCharacter(Object obj) {
//        if (obj instanceof Character character) {
//            return character;
//        }
//        return null;
//    }
//
//    public static Timestamp safeToTimestamp(Object obj) {
//        if (obj == null) {
//            return null;
//        }
//        return (Timestamp) obj;
//    }
//
//    public static Integer safeToInteger(Object obj) {
//        if (obj == null) {
//            return null;
//        }
//        if (obj instanceof Integer) {
//            return (Integer) obj;
//        }
//        if (obj instanceof String) {
//            try {
//                return Integer.parseInt(obj.toString().trim());
//            } catch (NumberFormatException e) {
//                // Log error if needed
//                return null;
//            }
//        }
//        if (obj instanceof Number) {
//            return ((Number) obj).intValue();
//        }
//        return null;
//    }
//
//    public static Short safeToShort(Object obj) {
//        if (obj == null) {
//            return null;
//        }
//        if (obj instanceof Short) {
//            return (Short) obj;
//        }
//        if (obj instanceof String) {
//            try {
//                return Short.parseShort(((String) obj).trim());
//            } catch (NumberFormatException e) {
//                // Log error if needed
//                return null;
//            }
//        }
//        if (obj instanceof Number) {
//            return ((Number) obj).shortValue();
//        }
//        return null;
//    }
//
//    public static Byte safeToByte(Object obj) {
//        if (obj == null) {
//            return null;
//        }
//        if (obj instanceof Byte) {
//            return (Byte) obj;
//        }
//        if (obj instanceof String) {
//            try {
//                return Byte.parseByte(((String) obj).trim());
//            } catch (NumberFormatException e) {
//                // Log error if needed
//                return null;
//            }
//        }
//        if (obj instanceof Number) {
//            return ((Number) obj).byteValue();
//        }
//        return null;
//    }
//
//    public static Long safeToLong(Object obj1) {
//        Long result = null;
//        if (obj1 != null) {
//            if (obj1 instanceof BigDecimal) {
//                return ((BigDecimal) obj1).longValue();
//            }
//            if (obj1 instanceof BigInteger) {
//                return ((BigInteger) obj1).longValue();
//            }
//            if (obj1 instanceof String) {
//                try {
//                    return Long.parseLong(((String) obj1).trim());
//                } catch (NumberFormatException e) {
//                    // Log error if needed
//                    return null;
//                }
//            }
//            try {
//                result = Long.parseLong(obj1.toString());
//            } catch (Exception ignored) {
//            }
//        }
//
//        return result;
//    }
//
//    public static Float safeToFloat(Object obj1) {
//        Float result = null;
//        if (obj1 != null) {
//            if (obj1 instanceof BigDecimal) {
//                return ((BigDecimal) obj1).floatValue();
//            }
//            if (obj1 instanceof BigInteger) {
//                return ((BigInteger) obj1).floatValue();
//            }
//            if (obj1 instanceof String) {
//                try {
//                    return Float.parseFloat(((String) obj1).trim());
//                } catch (NumberFormatException e) {
//                    // Log error if needed
//                    return null;
//                }
//            }
//            try {
//                result = Float.parseFloat(obj1.toString());
//            } catch (Exception ignored) {
//            }
//        }
//
//        return result;
//    }
//
//
//    public static BigInteger safeToBigInteger(Object obj) {
//        if (obj instanceof BigInteger) {
//            return (BigInteger) obj;
//        }
//        return null;
//    }
//
//    public static String timestampToString(Timestamp fromDate, String pattern) {
//        if (fromDate == null)
//            return "";
//        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
//        return sdf.format(fromDate);
//    }
//
//    public static String formatNumberWithComma(Double number, String pattern) {
//        if (number == null)
//            return "";
//        DecimalFormat df = new DecimalFormat(pattern);
//        return df.format(number);
//    }
//
//    public static String dateToString(Date fromDate, String pattern) {
//        if (fromDate == null)
//            return "";
//        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
//        return sdf.format(fromDate);
//    }
//
//    public static Double safeToDouble(Object obj1, Double defaultValue) {
//        Double result = defaultValue;
//        if (obj1 != null) {
//            try {
//                result = Double.parseDouble(obj1.toString());
//            } catch (Exception e) {
//                log.error(e.getMessage(), e);
//            }
//        }
//        return result;
//    }
//
//    public static Double safeToDouble(Object obj1) {
//        return safeToDouble(obj1, null);
//    }
//
//    public static Date safeToDate(Object obj) {
//        if (obj instanceof Date) {
//            return (Date) obj;
//        }
//        return null;
//    }
//
//    public static LocalDate safeToLocalDate(Object obj) {
//        if (obj instanceof LocalDate) {
//            return (LocalDate) obj;
//        }
//        return null;
//    }
//
//    public static String date2StringByPattern(Date date, String pattern) {
//        if (date == null || DataUtils.isNullOrEmpty(pattern)) {
//            return null;
//        }
//
//        DateFormat df = new SimpleDateFormat(pattern);
//        return df.format(date);
//    }
//
//    public static <T> List<T> convertListObjectsToClass(List<String> attConvert, List<Object[]> objects, Class<?> aClass) throws Exception {
//        List<T> lsResult = new ArrayList<>();
//        if (DataUtils.isNullOrEmpty(objects)) {
//            return lsResult;
//        } else {
//            for (Object[] item : objects) {
//                lsResult.add(convertObjectsToClass(attConvert, item, aClass));
//            }
//        }
//        return lsResult;
//    }
//
//    public static <T> T convertObjectsToClass(List<String> attConvert, Object[] objects, Class<?> aClass) throws Exception {
//        Object object = aClass.newInstance();
//        Field[] fields = aClass.getDeclaredFields();
//
//        for (int i = 0; i < attConvert.size(); i++) {
//
//            Field f;
//            int finalIndex = i;
//            f = Arrays.stream(fields).filter(item -> attConvert.get(finalIndex).equals(item.getName())).findFirst().orElse(null);
//            if (f != null) {
//                f.setAccessible(true);
//                Class<?> t = f.getType();
//                if (objects[i] == null)
//                    continue;
//                switch (t.getName()) {
//                    case "java.lang.String":
//                        if (objects[i] instanceof String || objects[i] instanceof Long || objects[i] instanceof BigInteger ||
//                                objects[i] instanceof Integer || objects[i] instanceof BigDecimal) {
//                            f.set(object, DataUtils.safeToString(objects[i]));
//                        } else if (objects[i] instanceof java.sql.Date || objects[i] instanceof Date
//                                || objects[i] instanceof Timestamp
//                        ) {
//                            f.set(object, date2StringByPattern(DataUtils.safeToDate(objects[i]), "dd/MM/yyyy HH:mm:ss"));
//                        }
//                        break;
//                    case "java.lang.Long":
//                    case "long":
//                        f.set(object, DataUtils.safeToLong(objects[i]));
//                        break;
//                    case "java.lang.Float":
//                    case "float":
//                        f.set(object, DataUtils.safeToFloat(objects[i]));
//                        break;
//                    case "java.lang.Double":
//                    case "double":
//                        f.set(object, DataUtils.safeToDouble(objects[i]));
//                        break;
//                    case "java.lang.Boolean":
//                    case "boolean":
//                        f.set(object, objects[i]);
//                        break;
//                    case "java.util.Date":
//                        f.set(object, DataUtils.safeToDate(objects[i]));
//                        break;
//                    case "java.time.LocalDate":
//                        f.set(object, DataUtils.safeToLocalDate(objects[i]));
//                        break;
//                    case "java.sql.Timestamp":
//                        f.set(object, DataUtils.safeToTimestamp(objects[i]));
//                        break;
//                    case "java.lang.Integer":
//                    case "int":
//                        f.set(object, DataUtils.safeToInteger(objects[i]));
//                        break;
//                    case "java.lang.Short":
//                        f.set(object, DataUtils.safeToShort(objects[i]));
//                        break;
//                    case "java.lang.Byte":
//                        f.set(object, DataUtils.safeToByte(objects[i]));
//                        break;
//                    case "java.math.BigInteger":
//                        f.set(object, DataUtils.safeToBigInteger(objects[i]));
//                        break;
//                    case "java.math.BigDecimal":
//                        f.set(object, DataUtils.safeToBigDecimal(objects[i]));
//                        break;
//                    case "java.lang.Character":
//                        f.set(object, DataUtils.safeToCharacter(objects[i]));
//                        break;
//                }
//            }
//        }
//        return (T) object;
//    }
//
//    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
//        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
//        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
//    }
//
//    public static String getFileNameReportUpdate(String preName) {
//        String pattern = "yyMMdd_HHmmss";
//        DateFormat dtf = new SimpleDateFormat(pattern);
//        String date = dtf.format(new Date());
//        return preName + "_" + date + ".xlsx";
//    }
//
//    public static String convertSqlLatin1ToUTF8Sequential(String input) {
//        if (input == null || input.isEmpty()) {
//            return input; // Return original input if null or empty
//        }
//
//        try {
//            // Convert from ISO-8859-1 to UTF-8 directly
//            byte[] bytes = input.getBytes("ISO-8859-1");
//            String result = new String(bytes, StandardCharsets.UTF_8);
//
//            if (result.contains("�")) {
//                bytes = input.getBytes("Windows-1252");
//                String resultT = new String(bytes, StandardCharsets.UTF_8);
//
//                result = replaceMisencodedChars(replaceString(result), replaceString(resultT));
//            }
//
//            if (result.contains("�") || result.contains("?")) return input;
//
//            return result;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return input;
//        }
//    }
//
//    private static String replaceMisencodedChars(String A, String B) {
//        StringBuilder result = new StringBuilder(A);
//        for (int i = 0; i < A.length(); i++) {
//            if (A.charAt(i) == '�' && i < B.length()) {
//                result.setCharAt(i, B.charAt(i));
//            }
//        }
//        return result.toString();
//    }
//
//    public static String replaceString(String input) {
//        return input.replaceAll("�\\?", "�");
//    }
//
////    public static Map<Integer, String> mapHeadersToFields(Row headerRow, Map<String, String> headerMapping, Class<?> entityClass) {
////        Map<Integer, String> fieldMapping = new HashMap<>();
////        for (int col = 0; col < headerRow.getLastCellNum(); col++) {
////            String excelHeader = headerRow.getCell(col).getStringCellValue().trim();
////            if (headerMapping.containsKey(excelHeader)) {
////                String fieldName = headerMapping.get(excelHeader);
////                try {
////                    if (entityClass.getDeclaredField(fieldName) != null) {
////                        fieldMapping.put(col, fieldName);
////                    }
////                } catch (NoSuchFieldException ignored) {
////                    // Ignore if field does not exist
////                }
////            }
////        }
////        return fieldMapping;
////    }
////
////    public static void mapRowToEntity(Row row, Map<Integer, String> fieldMapping, Object entity) {
////        fieldMapping.forEach((colIndex, fieldName) -> {
////            try {
////                Field field = entity.getClass().getDeclaredField(fieldName);
////                field.setAccessible(true);
////                Cell cell = row.getCell(colIndex);
////                if (cell != null) {
////                    setFieldValueFromCell(field, entity, cell);
////                }
////            } catch (Exception e) {
////                throw new IllegalArgumentException(
////                        String.format("Lỗi khi ánh xạ dữ liệu từ file Excel vào trường '%s'.", fieldName), e);
////            }
////        });
////    }
////
////    private static void setFieldValueFromCell(Field field, Object entity, Cell cell) throws IllegalAccessException {
////        try {
////            switch (cell.getCellType()) {
////                case 1:
////                    String cellValue = cell.getStringCellValue().trim();
////                    if (field.getType().equals(Date.class)) {
////                        try {
////                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
////                            field.set(entity, dateFormat.parse(cellValue));
////                        } catch (Exception e) {
////                            throw new IllegalArgumentException(
////                                    String.format("Lỗi khi parse ngày tháng tại cột %d: %s", cell.getStringCellValue(), e.getMessage())
////                            );
////                        }
////                    } else {
////                        field.set(entity, cellValue);
////                    }
////                    break;
////                case 0:
////                    if (DateUtil.isCellDateFormatted(cell)) { // Check if the cell contains a date
////                        field.set(entity, cell.getDateCellValue());
////                    } else if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
////                        field.set(entity, (int) cell.getNumericCellValue());
////                    } else if (field.getType().equals(double.class) || field.getType().equals(Double.class)) {
////                        field.set(entity, cell.getNumericCellValue());
////                    } else if (field.getType().equals(String.class)) { // Convert numeric value to String
////                        field.set(entity, String.valueOf((long) cell.getNumericCellValue()));
////                    } else {
////                        field.set(entity, cell.getNumericCellValue());
////                    }
////                    break;
////                default:
////                    // Handle other cell types if necessary
////                    break;
////            }
////        } catch (Exception ex) {
////            ex.printStackTrace();
////            throw new IllegalArgumentException("Lỗi khi ánh xạ dữ liệu từ file Excel.");
////        }
////    }
//
//    public static <T> List<T> getExportData(List<Object[]> rawList, List<String> keys, Class<T> clazz) throws Exception {
//        return DataUtils.convertListObjectsToClass(new ArrayList<>(keys), rawList, clazz);
//    }
//
//    public static String removeDiacritics(String input) {
//        if (input == null) return null;
//        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
//        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
//        return pattern.matcher(normalized).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
//    }
//
//    public static byte[] convertDocxToPdf(byte[] docxBytes) throws Exception {
//        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//        factory.setConnectTimeout(10000);
//        factory.setReadTimeout(10000);
//
//        RestTemplate restTemplate = new RestTemplate(factory);
//
//        String url = "http://libreoffice-service:8090/convert/bytes";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//
//        HttpEntity<byte[]> requestEntity = new HttpEntity<>(docxBytes, headers);
//
//        try {
//            ResponseEntity<byte[]> response = restTemplate.exchange(
//                    url,
//                    HttpMethod.POST,
//                    requestEntity,
//                    byte[].class
//            );
//
//            if (response.getStatusCode() == HttpStatus.OK) {
//                return response.getBody();
//            } else {
//                throw new IOException("Conversion failed with status: " + response.getStatusCode());
//            }
//        } catch (Exception e) {
//            throw new IOException("LibreOffice service error: " + e.getMessage(), e);
//        }
//    }
//
////    public static byte[] convertDocxToPdf(byte[] docxBytes) throws Exception {
////        File tempDocx = File.createTempFile("course-", ".docx");
////        Files.write(tempDocx.toPath(), docxBytes);
////
////        File outputDir = Files.createTempDirectory("pdf-out").toFile();
////
////        // NOTE: nên để path này config theo env
////        ProcessBuilder pb = new ProcessBuilder(
////                "C:\\Program Files\\LibreOffice\\program\\soffice.exe",
////                "--headless",
////                "--nologo",
////                "--nofirststartwizard",
////                "--nodefault",
////                "--convert-to", "pdf",
////                "--outdir", outputDir.getAbsolutePath(),
////                tempDocx.getAbsolutePath()
////        );
////
////        pb.redirectErrorStream(true);
////        Process process = pb.start();
////
////        String output;
////        try (var in = process.getInputStream()) {
////            output = new String(in.readAllBytes());
////        }
////
////        int exit = process.waitFor();
////        if (exit != 0) {
////            throw new IOException("LibreOffice convert failed, exitCode=" + exit + ", log=" + output);
////        }
////
////        // tìm file pdf trong outputDir (đừng hardcode tên)
////        File[] pdfs = outputDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
////        if (pdfs == null || pdfs.length == 0) {
////            throw new IOException("Không tạo được file PDF từ LibreOffice. Log=" + output);
////        }
////
////        // nếu có nhiều file thì lấy file mới nhất
////        File pdfFile = java.util.Arrays.stream(pdfs)
////                .max(java.util.Comparator.comparingLong(File::lastModified))
////                .orElseThrow();
////
////        byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
////
////        // cleanup
////        try { Files.deleteIfExists(tempDocx.toPath()); } catch (Exception ignored) {}
////        try { Files.deleteIfExists(pdfFile.toPath()); } catch (Exception ignored) {}
////        try { Files.deleteIfExists(outputDir.toPath()); } catch (Exception ignored) {}
////
////        return pdfBytes;
////    }
//
//
//    private static void setFieldValueFromCell(Field field, Object entity, Cell cell) throws IllegalAccessException {
//        try {
//            Class<?> fieldType = field.getType();
//
//            switch (cell.getCellType()) {
//                case STRING:
//                    String cellValue = cell.getStringCellValue().trim();
//
//                    if (cellValue.isEmpty()) {
//                        field.set(entity, null);
//                        break;
//                    }
//
//                    if (fieldType.equals(String.class)) {
//                        field.set(entity, cellValue);
//
//                    } else if (fieldType.equals(Date.class)) {
//                        try {
//                            SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
//                            dateFormat.setLenient(false);
//                            field.set(entity, dateFormat.parse(cellValue));
//                        } catch (Exception e) {
//                            throw new IllegalArgumentException(
//                                    String.format("Lỗi khi parse ngày tháng tại cột %d: %s", cell.getColumnIndex(), e.getMessage())
//                            );
//                        }
//
//                    } else if (fieldType.equals(BigDecimal.class)) {
//                        try {
//                            String cleanValue = cellValue.replaceAll("[^0-9.-]", "");
//                            field.set(entity, new BigDecimal(cleanValue));
//                        } catch (Exception e) {
//                            throw new IllegalArgumentException(
//                                    String.format("Lỗi khi parse BigDecimal tại cột %d: %s", cell.getColumnIndex(), e.getMessage())
//                            );
//                        }
//
//                    } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
//                        try {
//                            String cleanValue = cellValue.replaceAll("[^0-9-]", "");
//                            field.set(entity, Integer.parseInt(cleanValue));
//                        } catch (Exception e) {
//                            throw new IllegalArgumentException(
//                                    String.format("Lỗi khi parse Integer tại cột %d: %s", cell.getColumnIndex(), e.getMessage())
//                            );
//                        }
//
//                    } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
//                        try {
//                            String cleanValue = cellValue.replaceAll("[^0-9-]", "");
//                            field.set(entity, Long.parseLong(cleanValue));
//                        } catch (Exception e) {
//                            throw new IllegalArgumentException(
//                                    String.format("Lỗi khi parse Long tại cột %d: %s", cell.getColumnIndex(), e.getMessage())
//                            );
//                        }
//
//                    } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
//                        try {
//                            String cleanValue = cellValue.replaceAll("[^0-9.-]", "");
//                            field.set(entity, Double.parseDouble(cleanValue));
//                        } catch (Exception e) {
//                            throw new IllegalArgumentException(
//                                    String.format("Lỗi khi parse Double tại cột %d: %s", cell.getColumnIndex(), e.getMessage())
//                            );
//                        }
//
//                    } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
//                        field.set(entity, Boolean.parseBoolean(cellValue));
//
//                    } else {
//                        field.set(entity, cellValue);
//                    }
//                    break;
//
//                case NUMERIC:
//                    if (DateUtil.isCellDateFormatted(cell)) {
//                        if (fieldType.equals(Date.class)) {
//                            field.set(entity, cell.getDateCellValue());
//                        } else if (fieldType.equals(String.class)) {
//                            field.set(entity, new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue()));
//                        } else {
//                            field.set(entity, cell.getDateCellValue());
//                        }
//                    } else if (fieldType.equals(BigDecimal.class)) {
//                        field.set(entity, BigDecimal.valueOf(cell.getNumericCellValue()));
//                    } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
//                        field.set(entity, (int) cell.getNumericCellValue());
//                    } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
//                        field.set(entity, (long) cell.getNumericCellValue());
//                    } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
//                        field.set(entity, cell.getNumericCellValue());
//                    } else if (fieldType.equals(String.class)) {
//                        double numericValue = cell.getNumericCellValue();
//                        if (numericValue == (long) numericValue) {
//                            field.set(entity, String.valueOf((long) numericValue));
//                        } else {
//                            field.set(entity, String.valueOf(numericValue));
//                        }
//                    } else {
//                        field.set(entity, cell.getNumericCellValue());
//                    }
//                    break;
//
//                case BOOLEAN:
//                    if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
//                        field.set(entity, cell.getBooleanCellValue());
//                    } else if (fieldType.equals(String.class)) {
//                        field.set(entity, String.valueOf(cell.getBooleanCellValue()));
//                    } else {
//                        field.set(entity, cell.getBooleanCellValue());
//                    }
//                    break;
//
//                case FORMULA:
//                    FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
//                    CellValue formulaValue = evaluator.evaluate(cell);
//
//                    switch (formulaValue.getCellType()) {
//                        case NUMERIC:
//                            if (DateUtil.isCellDateFormatted(cell)) {
//                                if (fieldType.equals(Date.class)) {
//                                    field.set(entity, cell.getDateCellValue());
//                                } else if (fieldType.equals(String.class)) {
//                                    field.set(entity, new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue()));
//                                }
//                            } else if (fieldType.equals(BigDecimal.class)) {
//                                field.set(entity, BigDecimal.valueOf(formulaValue.getNumberValue()));
//                            } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
//                                field.set(entity, (int) formulaValue.getNumberValue());
//                            } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
//                                field.set(entity, (long) formulaValue.getNumberValue());
//                            } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
//                                field.set(entity, formulaValue.getNumberValue());
//                            } else if (fieldType.equals(String.class)) {
//                                double numericValue = formulaValue.getNumberValue();
//                                if (numericValue == (long) numericValue) {
//                                    field.set(entity, String.valueOf((long) numericValue));
//                                } else {
//                                    field.set(entity, String.valueOf(numericValue));
//                                }
//                            } else {
//                                field.set(entity, formulaValue.getNumberValue());
//                            }
//                            break;
//
//                        case STRING:
//                            String formulaStr = formulaValue.getStringValue().trim();
//
//                            if (formulaStr.isEmpty()) {
//                                field.set(entity, null);
//                            } else if (fieldType.equals(String.class)) {
//                                field.set(entity, formulaStr);
//                            } else if (fieldType.equals(BigDecimal.class)) {
//                                String cleanValue = formulaStr.replaceAll("[^0-9.-]", "");
//                                field.set(entity, new BigDecimal(cleanValue));
//                            } else if (fieldType.equals(Date.class)) {
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
//                                dateFormat.setLenient(false);
//                                field.set(entity, dateFormat.parse(formulaStr));
//                            } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
//                                String cleanValue = formulaStr.replaceAll("[^0-9-]", "");
//                                field.set(entity, Integer.parseInt(cleanValue));
//                            } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
//                                String cleanValue = formulaStr.replaceAll("[^0-9-]", "");
//                                field.set(entity, Long.parseLong(cleanValue));
//                            } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
//                                String cleanValue = formulaStr.replaceAll("[^0-9.-]", "");
//                                field.set(entity, Double.parseDouble(cleanValue));
//                            } else {
//                                field.set(entity, formulaStr);
//                            }
//                            break;
//
//                        case BOOLEAN:
//                            if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
//                                field.set(entity, formulaValue.getBooleanValue());
//                            } else if (fieldType.equals(String.class)) {
//                                field.set(entity, String.valueOf(formulaValue.getBooleanValue()));
//                            }
//                            break;
//
//                        default:
//                            field.set(entity, null);
//                            break;
//                    }
//                    break;
//
//                case BLANK:
//                    field.set(entity, null);
//                    break;
//
//                default:
//                    field.set(entity, null);
//                    break;
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            throw new IllegalArgumentException(
//                    String.format("Lỗi khi ánh xạ dữ liệu từ file Excel tại cột %d, field %s: %s",
//                            cell.getColumnIndex(), field.getName(), ex.getMessage())
//            );
//        }
//    }
//
//
//    public static Map<Integer, String> mapHeadersToFields(Row headerRow, Map<String, String> headerMapping, Class<?> entityClass) {
//        Map<Integer, String> fieldMapping = new HashMap<>();
//        for (int col = 0; col < headerRow.getLastCellNum(); col++) {
//            String excelHeader = headerRow.getCell(col).getStringCellValue().trim();
//            if (headerMapping.containsKey(excelHeader)) {
//                String fieldName = headerMapping.get(excelHeader);
//                try {
//                    if (entityClass.getDeclaredField(fieldName) != null) {
//                        fieldMapping.put(col, fieldName);
//                    }
//                } catch (NoSuchFieldException ignored) {
//                    // Ignore if field does not exist
//                }
//            }
//        }
//        return fieldMapping;
//    }
//
//    public static String getCellStringValue(Row row, Map<Integer, String> fieldMapping, String fieldName) {
//        // Tìm index cột tương ứng fieldName trong file Excel
//        Integer colIndex = null;
//        for (Map.Entry<Integer, String> entry : fieldMapping.entrySet()) {
//            if (entry.getValue().equalsIgnoreCase(fieldName)) {
//                colIndex = entry.getKey();
//                break;
//            }
//        }
//        if (colIndex == null) return null;
//
//        Cell cell = row.getCell(colIndex);
//        return getCellStringValue(cell);
//    }
//
//    public static String getCellStringValue(Cell cell) {
//        if (cell == null) return null;
//
//        return switch (cell.getCellType()) {
//            case STRING -> cell.getStringCellValue().trim();
//            case NUMERIC -> {
//                if (DateUtil.isCellDateFormatted(cell)) {
//                    yield new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
//                } else {
//                    // loại bỏ .0 nếu là số nguyên
//                    double numericValue = cell.getNumericCellValue();
//                    if (numericValue == (long) numericValue) {
//                        yield String.valueOf((long) numericValue);
//                    } else {
//                        yield String.valueOf(numericValue);
//                    }
//                }
//            }
//            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
//            case FORMULA -> {
//                try {
//                    yield cell.getStringCellValue();
//                } catch (IllegalStateException e) {
//                    yield String.valueOf(cell.getNumericCellValue());
//                }
//            }
//            default -> null;
//        };
//    }
//
//
//    public static Workbook loadWorkbook(MultipartFile file) throws IOException {
//        try {
//            return new HSSFWorkbook(file.getInputStream());
//        } catch (Exception e) {
//            // Nếu fail, đọc lại file với format khác
//            return new XSSFWorkbook(file.getInputStream());
//        }
//    }
//
//    public static int setupStatusColumn(Row headerRow) {
//        int statusColIndex = headerRow.getLastCellNum();
//        Cell statusHeader = headerRow.createCell(statusColIndex);
//        statusHeader.setCellValue("Trạng thái import");
//        return statusColIndex;
//    }
//
//    public static void mapRowToEntity(Row row, Map<Integer, String> fieldMapping, Object entity) {
//        fieldMapping.forEach((colIndex, fieldName) -> {
//            try {
//                Field field = entity.getClass().getDeclaredField(fieldName);
//                field.setAccessible(true);
//                Cell cell = row.getCell(colIndex);
//                if (cell != null) {
//                    setFieldValueFromCell(field, entity, cell);
//                }
//            } catch (Exception e) {
//                throw new IllegalArgumentException(
//                        String.format("Lỗi khi ánh xạ dữ liệu từ file Excel vào trường '%s'.", fieldName), e);
//            }
//        });
//    }
//
//    public static String getCurrentUsername() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication != null && authentication.isAuthenticated()) {
//            Object principal = authentication.getPrincipal();
//
//            if (principal instanceof UserDetails) {
//                return ((UserDetails) principal).getUsername();
//            } else {
//                return principal.toString();
//            }
//        }
//
//        return "anonymous";
//    }
//}
//
