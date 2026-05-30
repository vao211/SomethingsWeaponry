# ⚔️ Cẩm nang Tích hợp Better Combat (Minecraft 1.21)

Tài liệu này tổng hợp toàn bộ thông số kỹ thuật để tích hợp vũ khí custom (VD: `somethingsweaponry`) vào hệ thống hitbox, combo và hoạt ảnh của mod Better Combat.

> [!info] Đường dẫn tiêu chuẩn
> Mọi file cấu hình vũ khí của Better Combat bắt buộc phải là file `.json` và đặt tại:
> `src/main/resources/data/<mod_id>/weapon_attributes/<item_id>.json`

---

## 1. Cấu trúc Kế thừa Nhanh (`parent`)
Cách tối ưu nhất cho 90% vũ khí thông thường. Thay vì tự viết chuỗi combo phức tạp, bạn gọi `"parent"` để "sao chép" toàn bộ hoạt ảnh, âm thanh và hitbox của vũ khí có sẵn, sau đó chỉ việc ghi đè các thông số cơ bản.

```json
{
  "parent": "bettercombat:dagger",
  "attributes": {
    "category": "dagger",
    "attack_range": 2.0,
    "pose": "",
    "two_handed": false
  }
}
```

### Danh sách các `parent` mặc định:

* **Vũ khí sắc bén:** `bettercombat:sword`, `bettercombat:katana`, `bettercombat:claymore` (trọng kiếm), `bettercombat:dagger` (dao găm), `bettercombat:rapier` (kiếm liễu), `bettercombat:cutlass` (kiếm hải tặc).
* **Vũ khí đập/chặt:** `bettercombat:axe` (rìu), `bettercombat:heavy_axe` (rìu chiến), `bettercombat:mace` (chùy), `bettercombat:hammer` (búa tạ).
* **Vũ khí cán dài:** `bettercombat:spear` (giáo), `bettercombat:halberd` (kích), `bettercombat:glaive` (đại đao), `bettercombat:trident` (đinh ba), `bettercombat:scythe` (lưỡi hái), `bettercombat:battlestaff` (côn).

---

## 2. Giải phẫu hệ thống Thuộc tính (`attributes`)

Đây là nơi định nghĩa bản chất vật lý và tư thế của vũ khí.

### A. Định danh vũ khí (`category`)

Hoạt động như "Căn cước công dân" của vũ khí. Rất quan trọng để game Vanilla và các mod RPG khác (như Simply Skills) nhận diện để áp dụng hiệu ứng/bùa chú tương ứng.

* **Giá trị hợp lệ:** `"sword"`, `"dagger"`, `"axe"`, `"mace"`, `"hammer"`, `"spear"`, `"halberd"`, `"glaive"`, `"trident"`, `"scythe"`, `"battlestaff"`, `"pickaxe"`, `"shovel"`, `"hoe"`.

> [!warning] Chú ý cơ chế Phá khiên
> Nếu vũ khí của bạn (ví dụ: Battle Axe) mang `"category": "axe"`, nó sẽ tự động thừa hưởng cơ chế đánh vô hiệu hóa khiên của Minecraft Vanilla. Nếu điền sai thành `"sword"`, nó sẽ không thể phá khiên.

### B. Tầm đánh (`attack_range`)

Tính bằng số block. (Vanilla sword mặc định là `2.5`, Spear thường là `3.5` - `4.0`).

### C. Tư thế cánh tay (`pose`)

Quy định tư thế các khớp tay của nhân vật khi **đứng yên**.

* `""` (Để trống chữ): Cánh tay vung vẩy bình thường (Dùng cho vũ khí 1 tay như Dagger, Sword).
* `"two_handed_sword"`: Hai tay cầm chéo trước ngực (Katana).
* `"two_handed_heavy"`: Vác lên vai hoặc cầm trĩu xuống (Battle Axe, Hammer).
* `"two_handed_polearm"`: Cầm dọc, chĩa mũi vũ khí ra trước (Spear, Halberd).

> [!danger] Pose KHÔNG xoay vũ khí!
> Thuộc tính `pose` chỉ điều khiển **cánh tay người chơi**. Nếu bạn muốn cầm ngược dao găm (mũi dao chúc xuống đất), bạn không thể dùng `pose`. Việc xoay trục vật phẩm phải được thực hiện thông qua block `"display"` trong file **Item Model JSON** (nằm ở `models/item/`).

### D. Cơ chế Khóa tay trái (`two_handed`)

* `true`: Vô hiệu hóa tay trái. Người chơi không thể sử dụng khiên, đuốc hay vật phẩm ở off-hand khi đang cầm vũ khí này.
* `false`: Cho phép dùng khiên bình thường.

---

## 3. Cấu hình Chuỗi Combo Nâng cao (`attacks`)

Dùng khi bạn tạo Vũ khí Boss hoặc muốn kết hợp các nhịp đánh độc lạ (không dùng `parent`).

```json
{
  "attributes": {
    "category": "sword",
    "attack_range": 3.0,
    "pose": "two_handed_heavy",
    "two_handed": true,
    "attacks": [
      {
        "hitbox": "horizontal_plane",
        "damage_multiplier": 0.8,
        "angle": 120.0,
        "upswing": 0.5,
        "animation": "bettercombat:two_handed_slash_horizontal_right",
        "swing_sound": { "id": "minecraft:entity.player.attack.sweep" },
        "impact_sound": { "id": "minecraft:item.shield.block" }
      },
      {
        "hitbox": "vertical_plane",
        "damage_multiplier": 1.5,
        "angle": 90.0,
        "upswing": 0.6,
        "animation": "bettercombat:two_handed_slam"
      }
    ]
  }
}

```

### Các thông số của một nhịp chém (Attack Phase):

1. **`hitbox`**: Vùng sát thương thực tế.
* `"horizontal_plane"` (Quét ngang - Dọn quái).
* `"vertical_plane"` (Chém dọc - Hẹp nhưng cao).
* `"point"` (Đâm chọt - Cực hẹp, xa, dùng cho giáo/dao).


2. **`damage_multiplier`**: Hệ số nhân sát thương cho riêng nhịp đó (VD: `1.5` = 150% sát thương gốc của vũ khí).
3. **`angle`**: Góc quét sát thương tính bằng độ. (Nếu là `point` thì điền `0.0`).
4. **`upswing`**: Thời gian "niệm" (tính bằng giây) trước khi gây sát thương thực tế. Vũ khí càng nặng, upswing càng cao.
5. **`animation`**: File hoạt ảnh (`.blend`) hiển thị đòn chém.

### Danh sách Animation ID gốc (Dùng để mix combo):

* **Đánh 1 tay:** `one_handed_slash_horizontal_right`, `one_handed_slash_horizontal_left`, `one_handed_slash_vertical_right`, `one_handed_slash_vertical_left`, `one_handed_stab`.
* **Đánh 2 tay:** `two_handed_slash_horizontal_right`, `two_handed_slash_horizontal_left`, `two_handed_slash_vertical_right`, `two_handed_slash_vertical_left`, `two_handed_stab_right`, `two_handed_stab_left`, `two_handed_slam` (bổ gầm xuống đất).
* **Đánh kép (Dual Wielding):** `dual_handed_slash_cross` (chém chữ X), `dual_handed_slash_uncross` (vung tách ra).

