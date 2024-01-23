package jpabook.jpashop.service;

import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional //이걸 안붙여주면 저장이 안된다.
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    @Transactional
    //DirtyChecking으로 데이터 수정
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
        //이 코드가 끝나게 되면 transactional이 commit이 되고 jpa가 값의 변경사항을 인지하고 flush를 db에 날려 반영시켜준다
        //위의 코드를 jpa가 한줄로 요약해준 것이 merge이다.
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }


}
