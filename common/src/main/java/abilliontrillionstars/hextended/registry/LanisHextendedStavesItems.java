package abilliontrillionstars.hextended.registry;

import abilliontrillionstars.hextended.items.ItemBatteryStaff;
import abilliontrillionstars.hextended.items.ItemDrawingOrb;
import abilliontrillionstars.hextended.items.ItemExtendedAmethystStaff;
import abilliontrillionstars.hextended.items.ItemExtendedStaff;
import abilliontrillionstars.hextended.items.bookbinding.ItemBoundSpellbook;
import abilliontrillionstars.hextended.items.bookbinding.ItemSpellbookCover;
import at.petrak.hexcasting.common.items.ItemStaff;
import com.google.common.base.Suppliers;
import dev.architectury.platform.Platform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static abilliontrillionstars.hextended.LanisHextendedStaves.id;
import static at.petrak.hexcasting.common.lib.hex.HexActions.make;

public class LanisHextendedStavesItems
{
    public static void registerItems(BiConsumer<Item, ResourceLocation> r) {
        for (var e : ITEMS.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }
    public static void registerItemCreativeTab(CreativeModeTab.Output r, CreativeModeTab tab) {
        for (var item : ITEM_TABS.getOrDefault(tab, List.of())) {
            item.register(r);
        }
    }

    private static final Map<ResourceLocation, Item> ITEMS = new LinkedHashMap<>(); // preserve insertion order
    private static final Map<CreativeModeTab, List<LanisHextendedStavesItems.TabEntry>> ITEM_TABS = new LinkedHashMap<>();

    public static void init()
    {
        registerExtendedStaves();
        registerConditionalItems();
    }

    public static final ItemStaff MOSS_STAFF = makeStaff("moss", new ItemStaff(new Item.Properties().stacksTo(1)));
    public static final ItemStaff FLOWERED_MOSS_STAFF = makeStaff("flowered_moss", new ItemStaff(new Item.Properties().stacksTo(1)));
    public static final ItemStaff PRISMARINE_STAFF = makeStaff("prismarine", new ItemStaff(new Item.Properties().stacksTo(1)));
    public static final ItemStaff DARK_PRISMARINE_STAFF = makeStaff("dark_prismarine", new ItemStaff(new Item.Properties().stacksTo(1)));
    public static final ItemStaff OBSIDIAN_STAFF = makeStaff("obsidian", new ItemStaff(new Item.Properties().stacksTo(1)));
    public static final ItemStaff PURPUR_STAFF = makeStaff("purpur", new ItemStaff(new Item.Properties().stacksTo(1)));

    public static final ItemStaff LESSER_BATTERY_STAFF = makeStaff("lesser_battery", new ItemBatteryStaff(new Item.Properties().stacksTo(1)));
    public static final ItemStaff SEALED_LESSER_BATTERY_STAFF = makeStaff("sealed_lesser_battery", new ItemStaff(new Item.Properties().stacksTo(1)));
    public static final ItemStaff LESSER_BATTERY_EXTENDED_STAFF = makeLongStaff("lesser_battery", new ItemExtendedAmethystStaff(new Item.Properties().stacksTo(1)));
    public static final ItemStaff SEALED_LESSER_BATTERY_EXTENDED_STAFF = makeLongStaff("sealed_lesser_battery", new ItemExtendedStaff(new Item.Properties().stacksTo(1)));

    public static final ItemStaff EXTENDED_QUENCHED_STAFF = makeLongStaff("quenched", new ItemExtendedStaff(new Item.Properties().stacksTo(1)));

    public static final ItemDrawingOrb DRAWING_ORB = makeStaff("drawing_orb", new ItemDrawingOrb(new Item.Properties().stacksTo(1)));

    public static final ItemSpellbookCover SPELLBOOK_COVER = make("spellbook_cover", new ItemSpellbookCover(new Item.Properties().stacksTo(Item.MAX_STACK_SIZE)));
    public static final ItemBoundSpellbook BOUND_SPELLBOOK_TEST = make("bound_spellbook", new ItemBoundSpellbook(new Item.Properties().stacksTo(1)));


    private static void registerConditionalItems()
    {
        if (Platform.isModLoaded("hexcasting"))
            System.out.println("GOOOOOOOOOD evening everybody! this is the LOVELY hextended coming to you UH-LIVE from the soon-to-be-former Registration Event! How are y'all doin' tonight?");

    }

    private static void registerExtendedStaves()
    {
        String[] EXTENDED_STAFF_IDS = {"oak", "spruce", "birch", "jungle", "dark_oak", "acacia",
                "crimson", "warped", "mangrove", "bamboo", "cherry", "edified", "mindsplice",
                "moss", "flowered_moss", "prismarine", "dark_prismarine", "obsidian", "purpur"};
        for(String id : EXTENDED_STAFF_IDS)
            makeLongStaff(id, new ItemExtendedStaff(new Item.Properties().stacksTo(1)));
    }

    private static <T extends Item> T make(ResourceLocation id, T item, @Nullable CreativeModeTab tab) {
        var old = ITEMS.put(id, item);
        if (old != null)
            throw new IllegalArgumentException("Typo? Duplicate id " + id);
        if (tab != null)
            ITEM_TABS.computeIfAbsent(tab, t -> new ArrayList<>()).add(new LanisHextendedStavesItems.TabEntry.ItemEntry(item));
        return item;
    }
    private static <T extends Item> T make(String id, T item) { return make(id(id), item, LanisHextendedStavesCreativeTabs.STAVES); }
    private static <T extends Item> T makeStaff(String id, T item) { return make(id("staff/" + id), item, LanisHextendedStavesCreativeTabs.STAVES); }
    private static <T extends Item> T makeLongStaff(String id, T item) { return make(id("staff/long/" + id), item, LanisHextendedStavesCreativeTabs.STAVES); }

    private static Supplier<ItemStack> addToTab(Supplier<ItemStack> stack, CreativeModeTab tab) {
        var memoised = Suppliers.memoize(stack::get);
        ITEM_TABS.computeIfAbsent(tab, t -> new ArrayList<>()).add(new LanisHextendedStavesItems.TabEntry.StackEntry(memoised));
        return memoised;
    }

    private static abstract class TabEntry {
        abstract void register(CreativeModeTab.Output r);

        static class ItemEntry extends LanisHextendedStavesItems.TabEntry
        {
            private final Item item;

            ItemEntry(Item item) {
                this.item = item;
            }

            @Override
            void register(CreativeModeTab.Output r) {
                r.accept(item);
            }
        }

        static class StackEntry extends LanisHextendedStavesItems.TabEntry
        {
            private final Supplier<ItemStack> stack;

            StackEntry(Supplier<ItemStack> stack) {
                this.stack = stack;
            }

            @Override
            void register(CreativeModeTab.Output r) {
                r.accept(stack.get());
            }
        }
    }
}
