package games.moegirl.sinocraft.sinocalligraphy.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlayerSouvenirs {
    protected List<SouvenirEntry> souvenirs = new ArrayList<>();

    public List<SouvenirEntry> getSouvenirs() {
        return souvenirs;
    }

    public boolean hasSouvenir(ResourceLocation id) {
        return souvenirs.stream().anyMatch(s -> s.id.equals(id));
    }

    public int getSouvenir(ResourceLocation id) {
        if (hasSouvenir(id)) {
            return souvenirs.stream().filter(s -> s.id.equals(id)).findFirst().get().number;
        }
        return -1;
    }

    public void save(CompoundTag compound) {
        var list = getListSafe(compound);

        for (var s : souvenirs) {
            var nbt = new CompoundTag();
            nbt.putString("id", s.id.toString());
            nbt.putInt("number", s.number);
            list.add(nbt);
        }

        saveSafe(compound, list);
    }

    public void load(CompoundTag compound) {
        var s = getListSafe(compound);

        for (var tag : s) {
            if (tag instanceof CompoundTag nbt) {
                var rl = new ResourceLocation(nbt.getString("id"));
                var number = nbt.getInt("number");

                souvenirs.add(new SouvenirEntry(rl, number));
            }
        }
    }

    public void saveSafe(CompoundTag tag, ListTag list) {
        tag.getCompound("sinoseries")
                .getCompound("sinocalligraphy")
                .put("souvenirs", list);
    }

    public ListTag getListSafe(CompoundTag tag) {
        if (!tag.contains("sinoseries")) {
            tag.put("sinoseries", new CompoundTag());
        }

        var ss = tag.getCompound("sinoseries");

        if (!ss.contains("sinocalligraphy")) {
            ss.put("sinocalligraphy", new CompoundTag());
        }

        var sca = ss.getCompound("sinocalligraphy");

        if (!sca.contains("souvenirs")) {
            sca.put("souvenirs", new ListTag());
        }

        var list = sca.getList("souvenirs", 10);

        var toRemove = new ArrayList<Tag>();
        for (var s : list) {
            if (s instanceof CompoundTag nbt) {
                if (!nbt.contains("id")) {
                    toRemove.add(s);
                }

                if (!nbt.contains("number")) {
                    toRemove.add(s);
                }
            } else {
                toRemove.add(s);
            }
        }

        list.removeAll(toRemove);

        return list;
    }

    public static @NotNull PlayerSouvenirs from(@NotNull PlayerSouvenirs souvenir) {
        var ps = new PlayerSouvenirs();
        ps.souvenirs.addAll(souvenir.souvenirs);
        return ps;
    }

    private record SouvenirEntry(ResourceLocation id, int number) {
    }
}
