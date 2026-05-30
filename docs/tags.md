`Trong hệ thống Data-Driven của Minecraft, chúng ta không dùng khái niệm "Kế thừa class" (Inheritance) của Java/Kotlin để nhóm các vũ khí lại với nhau nữa. Thay vào đó, chúng ta sẽ dùng **Hệ thống Tag (Thẻ phân loại)**
Dưới đây là sơ đồ kiến trúc tổng thể để bạn thiết lập hệ thống tag cho `somethingsweaponry`:

### Bước 1: Tạo các Tag "Lớp con" (Sub-classes)

Đầu tiên, bạn tạo các file JSON đại diện cho từng nhóm vũ khí cụ thể.

Đường dẫn: `src/main/resources/data/somethingsweaponry/tags/item/`

Ví dụ file `daggers.json` của bạn sẽ trông như thế này:

JSON

```
{
  "replace": false,
  "values": [
    "somethingsweaponry:iron_dagger",
    "somethingsweaponry:diamond_dagger",
    "somethingsweaponry:netherite_dagger"
  ]
}
```

_(Bạn làm tương tự tạo ra các file `swords.json`, `heavy_swords.json`, `hammers.json`... chứa danh sách ID vũ khí tương ứng)._

### Bước 2: Tạo Tag "Lớp tổng" (The Base Class)

Bây giờ, thay vì phải gõ lại ID của từng món vũ khí một, bạn chỉ cần gom tất cả các file Tag ở Bước 1 vào chung một file `weapons.json`.

Đường dẫn: `src/main/resources/data/somethingsweaponry/tags/item/weapons.json`


```JSON
{
  "replace": false,
  "values": [
    "#somethingsweaponry:swords",
    "#somethingsweaponry:heavy_swords",
    "#somethingsweaponry:axes",
    "#somethingsweaponry:hammers",
    "#somethingsweaponry:spears",
    "#somethingsweaponry:tridents",
    "#somethingsweaponry:daggers"
  ]
}
```

**Lợi ích:** Từ giờ trở đi, bất cứ khi nào bạn viết một phù phép mới dành cho "mọi loại vũ khí", bạn chỉ cần gọi `#somethingsweaponry:weapons`. Nếu sau này bạn thêm nhóm `whips` (roi da) vào file này, bùa đó tự động hỗ trợ roi da mà không cần sửa code!

---

### Bước 3: "Kế thừa" vào hệ thống của Minecraft Vanilla

Đây là bước quan trọng nhất để trả lời câu hỏi thứ hai của bạn. Làm sao để Minecraft hiểu Spear và Dagger của bạn cũng là một dạng "Sword" để cho phép ép bùa Sắc bén (Sharpness) hay Quét kiếm (Sweeping Edge)?

Chìa khóa nằm ở thuộc tính `"replace": false`. Nó mang ý nghĩa: _"Đừng xóa danh sách cũ của game, hãy nối thêm danh sách của tôi vào"_.

Bạn cần can thiệp thẳng vào thư mục `minecraft` thay vì thư mục mod của bạn:

Đường dẫn: `src/main/resources/data/minecraft/tags/item/enchantable/sword.json`

JSON

```
{
  "replace": false,
  "values": [
    "#somethingsweaponry:swords",
    "#somethingsweaponry:spears",
    "#somethingsweaponry:daggers"
  ]
}
```

**Điều gì vừa xảy ra?**

Khi Minecraft nạp dữ liệu, nó sẽ tải file `sword.json` gốc của nó (chứa Kiếm kim cương, Kiếm sắt...). Sau đó, nó đọc được file `sword.json` của bạn. Nhờ `"replace": false`, nó vui vẻ gộp chung tag `#somethingsweaponry:daggers` và `#somethingsweaponry:spears` vào họ hàng nhà Kiếm.

Kết quả: Bàn phù phép sẽ tự động cung cấp mọi bùa của Kiếm (Sharpness, Smite, Bane of Arthropods, Looting) cho Giáo và Dao găm của bạn mà bạn không cần phải viết thêm một dòng code Java/Kotlin nào!

### 💡 Mở rộng (Hệ thống "c"):

Để đạt chuẩn mực tối đa của cộng đồng Fabric, bạn nên làm thêm một bước "nhúng" tag của mình vào tag chung (Common Tags).

Tạo file: `src/main/resources/data/c/tags/item/swords.json`

JSON

```
{
  "replace": false,
  "values": [
    "#somethingsweaponry:swords"
  ]
}
```