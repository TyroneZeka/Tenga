package com.tenga.config;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/** Populates the dev database with 50 test users and 1 000 realistic listings. */
@Order(2)
@Component
@Profile("dev")
public class DevDataSeeder implements CommandLineRunner {

  private static final Logger log = LoggerFactory.getLogger(DevDataSeeder.class);

  private static final int USER_COUNT = 50;
  private static final int LISTING_COUNT = 1_000;
  private static final String SEED_PASSWORD = "Test1234!";

  // ── Category UUIDs (hardcoded from R__seed_categories.sql) ──────────────────

  private static final String CAT_PHONES = "00000000-0000-0000-0002-000000000001";
  private static final String CAT_COMPUTERS = "00000000-0000-0000-0002-000000000002";
  private static final String CAT_TVS = "00000000-0000-0000-0002-000000000003";
  private static final String CAT_CAMERAS = "00000000-0000-0000-0002-000000000004";
  private static final String CAT_CARS = "00000000-0000-0000-0002-000000000010";
  private static final String CAT_MOTORCYCLES = "00000000-0000-0000-0002-000000000011";
  private static final String CAT_SPARE_PARTS = "00000000-0000-0000-0002-000000000013";
  private static final String CAT_FURNITURE = "00000000-0000-0000-0001-000000000003";
  private static final String CAT_CLOTHING = "00000000-0000-0000-0001-000000000004";
  private static final String CAT_HOME = "00000000-0000-0000-0001-000000000005";
  private static final String CAT_SPORTS = "00000000-0000-0000-0001-000000000006";
  private static final String CAT_AGRICULTURE = "00000000-0000-0000-0001-000000000009";

  // ── City / suburb data ───────────────────────────────────────────────────────

  private record CityData(String city, String suburb, double lat, double lng) {}

  private static final List<CityData> CITIES =
      List.of(
          new CityData("Harare", "Avondale", -17.8292, 31.0522),
          new CityData("Harare", "Borrowdale", -17.7630, 31.1000),
          new CityData("Harare", "Mbare", -17.8667, 31.0333),
          new CityData("Harare", "Hatfield", -17.8700, 31.1000),
          new CityData("Harare", "Budiriro", -17.9000, 30.9833),
          new CityData("Harare", "Greendale", -17.8200, 31.1200),
          new CityData("Bulawayo", "Suburbs", -20.1325, 28.6265),
          new CityData("Bulawayo", "Nkulumane", -20.1500, 28.5800),
          new CityData("Bulawayo", "Pumula", -20.1800, 28.5500),
          new CityData("Mutare", null, -18.9707, 32.6709),
          new CityData("Marondera", null, -18.1893, 31.5517),
          new CityData("Gweru", null, -19.4530, 29.8160),
          new CityData("Chitungwiza", null, -18.0127, 31.0527),
          new CityData("Kwekwe", null, -18.9203, 29.8144),
          new CityData("Kadoma", null, -18.3415, 29.9088),
          new CityData("Masvingo", null, -20.0608, 30.8337),
          new CityData("Chinhoyi", null, -17.3608, 30.1997),
          new CityData("Victoria Falls", null, -17.9243, 25.8572),
          new CityData("Bindura", null, -17.3000, 31.3333));

  // ── Title templates per category ─────────────────────────────────────────────

  private static final Map<String, List<String>> TITLES =
      Map.ofEntries(
          Map.entry(
              CAT_PHONES,
              List.of(
                  "iPhone %d Pro",
                  "iPhone %d",
                  "Samsung Galaxy A%d",
                  "Samsung Galaxy S%d",
                  "Tecno Camon %d",
                  "Tecno Spark %d",
                  "Infinix Hot %d",
                  "Infinix Note %d",
                  "Huawei P%d Lite",
                  "Redmi Note %d Pro",
                  "Nokia G%d")),
          Map.entry(
              CAT_COMPUTERS,
              List.of(
                  "HP Laptop %d",
                  "Dell Inspiron %d",
                  "Lenovo ThinkPad %d",
                  "Acer Aspire %d",
                  "HP EliteBook %d",
                  "MacBook Air %d",
                  "Dell XPS %d",
                  "Toshiba Satellite %d")),
          Map.entry(
              CAT_TVS,
              List.of(
                  "Samsung %d\" Smart TV",
                  "LG %d\" 4K TV",
                  "Sony Bravia %d\"",
                  "Hisense %d\" TV",
                  "TCL %d\" Smart TV",
                  "Skyworth %d\" TV")),
          Map.entry(
              CAT_CAMERAS,
              List.of(
                  "Canon EOS %d",
                  "Nikon D%d",
                  "Sony Alpha A%d",
                  "Canon PowerShot",
                  "GoPro Hero %d",
                  "DJI Mini %d Drone")),
          Map.entry(
              CAT_CARS,
              List.of(
                  "Toyota Corolla %d",
                  "Toyota Vitz %d",
                  "Honda Fit %d",
                  "Mazda Demio %d",
                  "Toyota Wish %d",
                  "Nissan Note %d",
                  "Suzuki Swift %d",
                  "Toyota RAV4 %d",
                  "Ford Ranger %d",
                  "Mitsubishi Pajero %d")),
          Map.entry(
              CAT_MOTORCYCLES,
              List.of(
                  "Honda CB%d",
                  "Yamaha YBR%d",
                  "Suzuki GSX%d",
                  "TVS Apache %d",
                  "Boxer %d Motorcycle",
                  "Zhing Zhong %d")),
          Map.entry(
              CAT_SPARE_PARTS,
              List.of(
                  "Toyota Corolla Gearbox",
                  "Engine Parts — Toyota",
                  "Bumper — Toyota Vitz",
                  "Honda Fit Alternator",
                  "Alloy Rims x4",
                  "Car Battery — 90Ah",
                  "Shock Absorbers — Toyota",
                  "Radiator — Ford Ranger")),
          Map.entry(
              CAT_FURNITURE,
              List.of(
                  "3-Piece Lounge Suite",
                  "5-Piece Lounge Suite",
                  "Queen Bed + Mattress",
                  "King Size Bed Frame",
                  "Dining Table + %d Chairs",
                  "Wardrobe — %d Doors",
                  "Bookshelf",
                  "Computer Desk",
                  "TV Stand",
                  "Coffee Table",
                  "Chest of Drawers",
                  "Baby Cot")),
          Map.entry(
              CAT_CLOTHING,
              List.of(
                  "Men's Suit — Size %d",
                  "Ladies Formal Dress",
                  "Nike Sneakers Size %d",
                  "Adidas Tracksuit",
                  "Leather Shoes — Size %d",
                  "Winter Jacket",
                  "Denim Jeans x%d Pairs",
                  "School Uniform Bundle",
                  "Ladies Handbag",
                  "Gentlemen's Briefcase")),
          Map.entry(
              CAT_HOME,
              List.of(
                  "Gas Stove + %d Burners",
                  "Electric Kettle",
                  "Microwave Oven",
                  "Stand Fan",
                  "Air Conditioner",
                  "Refrigerator",
                  "Washing Machine",
                  "Vacuum Cleaner",
                  "Pressure Cooker",
                  "Flat Iron")),
          Map.entry(
              CAT_SPORTS,
              List.of(
                  "Mountain Bicycle",
                  "Road Bicycle",
                  "Gym Equipment Set",
                  "Football Boots Size %d",
                  "Cricket Bat",
                  "Tennis Racket",
                  "Swimming Pool (%d x %d)",
                  "Treadmill",
                  "Dumbbell Set")),
          Map.entry(
              CAT_AGRICULTURE,
              List.of(
                  "Irrigation Pump",
                  "Water Pump — %dHP",
                  "Borehole Equipment",
                  "Solar Water Pump",
                  "%d x 50kg Bags Fertiliser",
                  "Tractor Tyres",
                  "Hand Sprayer",
                  "Knapsack Sprayer",
                  "Cattle Handling Equipment",
                  "Chicken Brooder")));

  // ── Condition & status distributions ─────────────────────────────────────────

  private static final String[] CONDITIONS = {
    "NEW",
    "NEW",
    "NEW",
    "LIKE_NEW",
    "LIKE_NEW",
    "LIKE_NEW",
    "LIKE_NEW",
    "LIKE_NEW",
    "GOOD",
    "GOOD",
    "GOOD",
    "GOOD",
    "GOOD",
    "GOOD",
    "GOOD",
    "FAIR",
    "FAIR",
    "FAIR",
    "FAIR",
    "POOR"
  };

  private static final String[] STATUSES = {
    "ACTIVE",
    "ACTIVE",
    "ACTIVE",
    "ACTIVE",
    "ACTIVE",
    "ACTIVE",
    "ACTIVE",
    "ACTIVE",
    "ACTIVE",
    "ACTIVE",
    "ACTIVE",
    "ACTIVE",
    "ACTIVE",
    "ACTIVE",
    "ACTIVE",
    "ACTIVE",
    "ACTIVE",
    "ACTIVE",
    "ACTIVE",
    "ACTIVE",
    "ACTIVE",
    "ACTIVE",
    "SOLD",
    "SOLD",
    "RESERVED"
  };

  // ── Category → loremflickr keyword (for category-appropriate images) ──────────

  private static final Map<String, String> IMAGE_KEYWORDS =
      Map.ofEntries(
          Map.entry(CAT_PHONES, "smartphone,phone"),
          Map.entry(CAT_COMPUTERS, "laptop,computer"),
          Map.entry(CAT_TVS, "television"),
          Map.entry(CAT_CAMERAS, "camera"),
          Map.entry(CAT_CARS, "car,automobile"),
          Map.entry(CAT_MOTORCYCLES, "motorcycle"),
          Map.entry(CAT_SPARE_PARTS, "car,engine"),
          Map.entry(CAT_FURNITURE, "furniture,interior"),
          Map.entry(CAT_CLOTHING, "fashion,clothing"),
          Map.entry(CAT_HOME, "kitchen,appliance"),
          Map.entry(CAT_SPORTS, "sport,fitness"),
          Map.entry(CAT_AGRICULTURE, "farm,tractor"));

  // ── Network prefixes for Zimbabwean phone numbers ─────────────────────────────

  private static final String[] NETWORK_PREFIXES = {"77", "78", "71", "73"};

  // ── Category weights (100 slots weighted) ────────────────────────────────────

  private static final List<String> WEIGHTED_CATEGORIES = buildWeightedCategories();

  private static List<String> buildWeightedCategories() {
    List<String> cats = new ArrayList<>();
    // Electronics 40%
    addN(cats, CAT_PHONES, 20);
    addN(cats, CAT_COMPUTERS, 8);
    addN(cats, CAT_TVS, 7);
    addN(cats, CAT_CAMERAS, 5);
    // Vehicles 15%
    addN(cats, CAT_CARS, 10);
    addN(cats, CAT_MOTORCYCLES, 3);
    addN(cats, CAT_SPARE_PARTS, 2);
    // Others 45%
    addN(cats, CAT_FURNITURE, 10);
    addN(cats, CAT_CLOTHING, 10);
    addN(cats, CAT_HOME, 8);
    addN(cats, CAT_SPORTS, 7);
    addN(cats, CAT_AGRICULTURE, 10);
    return cats;
  }

  private static void addN(List<String> list, String value, int n) {
    for (int i = 0; i < n; i++) list.add(value);
  }

  // ─────────────────────────────────────────────────────────────────────────────

  private final JdbcTemplate jdbc;
  private final PasswordEncoder passwordEncoder;

  public DevDataSeeder(JdbcTemplate jdbc, PasswordEncoder passwordEncoder) {
    this.jdbc = jdbc;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void run(String... args) {
    Integer listingCount = jdbc.queryForObject("SELECT COUNT(*) FROM lst_listings", Integer.class);
    Integer imageCount =
        jdbc.queryForObject("SELECT COUNT(*) FROM lst_listing_images", Integer.class);
    if (listingCount != null && listingCount >= 500 && imageCount != null && imageCount > 0) {
      log.info(
          "[DEV] Seed data already present ({} listings, {} images) — skipping",
          listingCount,
          imageCount);
      return;
    }

    Faker faker = new Faker(Locale.ENGLISH);
    Random random = new Random(42); // fixed seed for reproducible data

    if (listingCount == null || listingCount < 500) {
      List<UUID> userIds = seedUsers(faker, random);
      List<UUID> listingIds = seedListings(faker, random, userIds);
      seedImages(listingIds, random);
      log.info("[DEV] Seeded {} users, {} listings, and images", USER_COUNT, LISTING_COUNT);
    } else {
      // Listings exist but images are missing — seed images only
      List<UUID> listingIds =
          jdbc.query(
              "SELECT id FROM lst_listings ORDER BY created_at",
              (rs, n) -> UUID.fromString(rs.getString("id")));
      seedImages(listingIds, random);
      log.info("[DEV] Seeded images for {} existing listings", listingIds.size());
    }
  }

  // ── Step A: seed users ───────────────────────────────────────────────────────

  private List<UUID> seedUsers(Faker faker, Random random) {
    String passwordHash = passwordEncoder.encode(SEED_PASSWORD);
    List<UUID> userIds = new ArrayList<>(USER_COUNT);
    List<UUID[]> profileRows = new ArrayList<>(USER_COUNT); // [profileId, userId, cityIndex]

    for (int i = 0; i < USER_COUNT; i++) {
      userIds.add(UUID.randomUUID());
      profileRows.add(new UUID[] {UUID.randomUUID(), userIds.get(i)});
    }

    // Batch insert auth_users
    jdbc.batchUpdate(
        """
        INSERT INTO auth_users
            (id, phone_number, password_hash, role, provider,
             phone_verified, email_verified, enabled,
             created_at, updated_at, version)
        VALUES (?, ?, ?, 'SELLER', 'LOCAL', true, false, true, now(), now(), 0)
        """,
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setObject(1, userIds.get(i));
            ps.setString(2, generatePhone(random));
            ps.setString(3, passwordHash);
          }

          @Override
          public int getBatchSize() {
            return USER_COUNT;
          }
        });

    // Batch insert usr_profiles (with avatar_url)
    jdbc.batchUpdate(
        """
        INSERT INTO usr_profiles
            (id, user_id, display_name, city, avatar_url, created_at, updated_at, version)
        VALUES (?, ?, ?, ?, ?, now(), now(), 0)
        """,
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            CityData city = CITIES.get(random.nextInt(CITIES.size()));
            ps.setObject(1, profileRows.get(i)[0]);
            ps.setObject(2, profileRows.get(i)[1]);
            ps.setString(3, faker.name().fullName());
            ps.setString(4, city.city());
            // lock=i+1 so each user gets a consistent, distinct portrait
            ps.setString(5, "https://loremflickr.com/150/150/portrait?lock=" + (i + 1));
          }

          @Override
          public int getBatchSize() {
            return USER_COUNT;
          }
        });

    return userIds;
  }

  // ── Step B: seed listings ────────────────────────────────────────────────────

  private record ListingRow(
      UUID id,
      String title,
      String description,
      BigDecimal price,
      String currency,
      String condition,
      String status,
      UUID sellerId,
      String categoryId,
      String city,
      String suburb,
      double lat,
      double lng,
      boolean negotiable,
      Instant expiresAt) {}

  private List<UUID> seedListings(Faker faker, Random random, List<UUID> userIds) {
    List<ListingRow> rows = new ArrayList<>(LISTING_COUNT);

    for (int i = 0; i < LISTING_COUNT; i++) {
      String catId = WEIGHTED_CATEGORIES.get(random.nextInt(WEIGHTED_CATEGORIES.size()));
      CityData city = CITIES.get(random.nextInt(CITIES.size()));
      String condition = CONDITIONS[random.nextInt(CONDITIONS.length)];
      String status = STATUSES[random.nextInt(STATUSES.length)];
      UUID sellerId = userIds.get(random.nextInt(userIds.size()));

      double lat = city.lat() + (random.nextDouble() - 0.5) * 0.1;
      double lng = city.lng() + (random.nextDouble() - 0.5) * 0.1;

      String[] priceAndCurrency = generatePrice(catId, random);
      BigDecimal price = new BigDecimal(priceAndCurrency[0]);
      String currency = priceAndCurrency[1];

      String title = buildTitle(catId, random, faker);
      String description = faker.lorem().paragraph(2 + random.nextInt(3));
      boolean negotiable = random.nextInt(3) == 0; // ~33%

      Instant expiresAt = "ACTIVE".equals(status) ? Instant.now().plus(90, ChronoUnit.DAYS) : null;

      rows.add(
          new ListingRow(
              UUID.randomUUID(),
              title,
              description,
              price,
              currency,
              condition,
              status,
              sellerId,
              catId,
              city.city(),
              city.suburb(),
              lat,
              lng,
              negotiable,
              expiresAt));
    }

    jdbc.batchUpdate(
        """
        INSERT INTO lst_listings
            (id, title, description, price, currency, condition,
             status, seller_id, category_id, city, suburb,
             latitude, longitude, view_count, negotiable, expires_at,
             created_at, updated_at, version)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), now(), 0)
        """,
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ListingRow r = rows.get(i);
            ps.setObject(1, r.id());
            ps.setString(2, r.title());
            ps.setString(3, r.description());
            ps.setBigDecimal(4, r.price());
            ps.setString(5, r.currency());
            ps.setString(6, r.condition());
            ps.setString(7, r.status());
            ps.setObject(8, r.sellerId());
            ps.setObject(9, UUID.fromString(r.categoryId()));
            ps.setString(10, r.city());
            ps.setString(11, r.suburb());
            ps.setDouble(12, r.lat());
            ps.setDouble(13, r.lng());
            ps.setInt(14, random.nextInt(200)); // random view count
            ps.setBoolean(15, r.negotiable());
            ps.setTimestamp(16, r.expiresAt() != null ? Timestamp.from(r.expiresAt()) : null);
          }

          @Override
          public int getBatchSize() {
            return LISTING_COUNT;
          }
        });

    return rows.stream().map(ListingRow::id).toList();
  }

  // ── Step C: seed listing images ───────────────────────────────────────────────

  private record ImageRow(UUID listingId, String categoryId, int imageIndex, int lockBase) {}

  private void seedImages(List<UUID> listingIds, Random random) {
    // Re-fetch categoryId per listing to pick the right keyword
    List<Object[]> catRows =
        jdbc.query(
            "SELECT id, category_id FROM lst_listings WHERE id = ANY(?)",
            ps ->
                ps.setArray(
                    1,
                    ps.getConnection()
                        .createArrayOf("uuid", listingIds.stream().map(UUID::toString).toArray())),
            (rs, n) ->
                new Object[] {UUID.fromString(rs.getString("id")), rs.getString("category_id")});

    List<ImageRow> imageRows = new ArrayList<>();
    int lockCounter = 100; // start above avatar range (0-50)
    for (Object[] row : catRows) {
      UUID listingId = (UUID) row[0];
      String catId = (String) row[1];
      int imgCount = 1 + random.nextInt(3); // 1-3 images per listing
      for (int j = 0; j < imgCount; j++) {
        imageRows.add(new ImageRow(listingId, catId, j, lockCounter++));
      }
    }

    jdbc.batchUpdate(
        """
        INSERT INTO lst_listing_images
            (id, listing_id, storage_key, url, sort_order, created_at, updated_at, version)
        VALUES (?, ?, ?, ?, ?, now(), now(), 0)
        """,
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ImageRow r = imageRows.get(i);
            String keyword = IMAGE_KEYWORDS.getOrDefault(r.categoryId(), "product,item");
            String url = "https://loremflickr.com/600/400/" + keyword + "?lock=" + r.lockBase();
            ps.setObject(1, UUID.randomUUID());
            ps.setObject(2, r.listingId());
            ps.setString(3, "seed/listings/" + r.listingId() + "/image-" + r.imageIndex() + ".jpg");
            ps.setString(4, url);
            ps.setInt(5, r.imageIndex());
          }

          @Override
          public int getBatchSize() {
            return imageRows.size();
          }
        });
  }

  // ── Helpers ──────────────────────────────────────────────────────────────────

  private String generatePhone(Random random) {
    String prefix = NETWORK_PREFIXES[random.nextInt(NETWORK_PREFIXES.length)];
    // Generate 7 random digits, zero-padded
    int digits = 1_000_000 + random.nextInt(9_000_000);
    return "+263" + prefix + digits;
  }

  private String buildTitle(String catId, Random random, Faker faker) {
    List<String> templates = TITLES.getOrDefault(catId, List.of("Second-Hand Item"));
    String template = templates.get(random.nextInt(templates.size()));
    // Fill %d/%s placeholders with realistic numbers/strings
    return template
        .replaceAll("%d", String.valueOf(pickRealistic(catId, random)))
        .replaceAll("%s", String.valueOf(pickYear(catId, random)));
  }

  private int pickRealistic(String catId, Random random) {
    return switch (catId) {
      case CAT_PHONES -> 10 + random.nextInt(5); // 10-14 (model numbers)
      case CAT_COMPUTERS -> 300 + random.nextInt(700); // 300-999
      case CAT_TVS -> new int[] {32, 40, 43, 50, 55, 65}[random.nextInt(6)];
      case CAT_CARS, CAT_MOTORCYCLES -> 2010 + random.nextInt(14); // year
      case CAT_FURNITURE -> 4 + random.nextInt(4); // chairs / doors
      case CAT_CLOTHING -> 36 + random.nextInt(10); // size
      case CAT_HOME -> 2 + random.nextInt(3); // burners
      case CAT_AGRICULTURE -> 5 + random.nextInt(20); // bags / HP
      default -> 1 + random.nextInt(9);
    };
  }

  private int pickYear(String catId, Random random) {
    return 2010 + random.nextInt(14);
  }

  /** Returns [amountString, currency] */
  private String[] generatePrice(String catId, Random random) {
    return switch (catId) {
      case CAT_PHONES -> usdOrZig(random, 15, 580, 400, 18000, random);
      case CAT_COMPUTERS -> usdOrZig(random, 80, 700, 2500, 21000, random);
      case CAT_TVS -> usdOrZig(random, 50, 500, 1500, 15000, random);
      case CAT_CAMERAS -> usdOrZig(random, 30, 450, 900, 13500, random);
      case CAT_CARS -> new String[] {String.valueOf(1500 + random.nextInt(10500)), "USD"};
      case CAT_MOTORCYCLES -> new String[] {String.valueOf(400 + random.nextInt(2600)), "USD"};
      case CAT_SPARE_PARTS -> usdOrZig(random, 5, 200, 150, 6000, random);
      case CAT_FURNITURE -> usdOrZig(random, 20, 400, 600, 12000, random);
      case CAT_CLOTHING -> usdOrZig(random, 3, 55, 80, 1600, random);
      case CAT_HOME -> usdOrZig(random, 10, 350, 300, 10500, random);
      case CAT_SPORTS -> usdOrZig(random, 5, 250, 150, 7500, random);
      case CAT_AGRICULTURE -> zigOrUsd(random, 500, 8000, 15, 250, random);
      default -> usdOrZig(random, 5, 150, 150, 4500, random);
    };
  }

  /** 60% USD, 40% ZiG. */
  private String[] usdOrZig(Random r, int minUsd, int maxUsd, int minZig, int maxZig, Random rand) {
    if (r.nextInt(10) < 6) {
      return new String[] {String.valueOf(minUsd + rand.nextInt(maxUsd - minUsd + 1)), "USD"};
    }
    return new String[] {String.valueOf(minZig + rand.nextInt(maxZig - minZig + 1)), "ZIG"};
  }

  /** 60% ZiG, 40% USD. */
  private String[] zigOrUsd(Random r, int minZig, int maxZig, int minUsd, int maxUsd, Random rand) {
    if (r.nextInt(10) < 6) {
      return new String[] {String.valueOf(minZig + rand.nextInt(maxZig - minZig + 1)), "ZIG"};
    }
    return new String[] {String.valueOf(minUsd + rand.nextInt(maxUsd - minUsd + 1)), "USD"};
  }
}
