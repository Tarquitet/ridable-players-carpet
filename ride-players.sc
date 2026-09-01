__config() -> {
    'stay_loaded' -> true,
    'scope' -> 'global'
};

global_debug = true;
global_tick = 0;

global_child = {};
global_parent = {};
global_seats = {};

_find_player(uuid) -> (
    found = null;
    for(player('all'), if(_ ~ 'uuid' == uuid, found = _));
    found
);

_find_root(uu) -> (
    cur = uu;
    n = 0;
    while(has(global_parent, cur) && n < 64,
        cur = global_parent:cur;
        n = n + 1
    );
    cur
);

__on_start() -> run('kill @e[tag=fsitseat]');

_detach_by_uuid(ru) -> (
    if (!has(global_parent, ru), return());
    parent_u = global_parent:ru;
    child_u = global_child:ru;

    seat = global_seats:ru;
    if (seat != null, modify(seat, 'remove'));

    delete(global_seats, ru);
    delete(global_parent, ru);
    delete(global_child, ru);

    if (child_u != null,
        global_child:parent_u = child_u;
        global_parent:child_u = parent_u;
    ,
        delete(global_child, parent_u);
    );
);

_promote_first(root_u) -> (
    first_u = global_child:root_u;
    if (first_u == null, (
        delete(global_child, root_u);
        delete(global_parent, root_u);
        delete(global_seats, root_u);
        return()
    ));
    seat = global_seats:first_u;
    if (seat != null, modify(seat, 'remove'));
    delete(global_seats, first_u);
    delete(global_parent, first_u);
    delete(global_child, root_u);
    delete(global_parent, root_u);
    delete(global_seats, root_u);
);

_sweep_orphans() -> (
    known = {};
    for(values(global_seats), known:(_ ~ 'uuid') = true);
    for(entity_selector('@e[type=minecraft:armor_stand,tag=fsitseat]'), (
        if (!has(known, _ ~ 'uuid'), modify(_, 'remove'))
    ))
);

_attach(rider, base_player) -> (
    ru = rider ~ 'uuid';
    if (has(global_parent, ru), _detach_by_uuid(ru));

    top = base_player;
    top_u = top ~ 'uuid';

    n = 0;
    while(has(global_child, top_u) && n < 64,
        top_u = global_child:top_u;
        top = _find_player(top_u);
        n = n + 1;
    );
    if (top == null, return());

    seat = spawn('armor_stand', top ~ 'pos' + [0, 2.5, 0]);
    if (seat != null,
        modify(seat, 'nbt_merge', '{Marker:1b, Small:1b, DisabledSlots:4144959, NoBasePlate:1b, NoGravity:1b, Invisible:1b, Tags:["fsitseat"]}');
        run('attribute ' + seat ~ 'uuid' + ' minecraft:generic.scale base set 0.0001');

        if (modify(rider, 'mount', seat),
            global_child:top_u = ru;
            global_parent:ru = top_u;
            global_seats:ru = seat;
            if (global_debug, print(rider, '[FSIT] Montado al tope de la torre'));
        ,
            modify(seat, 'remove')
        )
    )
);

__on_tick() -> (
    global_tick = global_tick + 1;
    if (global_tick % 200 == 0, _sweep_orphans());

    for(player('all'), (
        q = _;
        qu = q ~ 'uuid';
        if (has(global_parent, qu), (
            root_u = _find_root(qu);
            if (_find_player(root_u) == null, _promote_first(root_u))
        ))
    ));

    for(player('all'),
        p = _;
        pu = p ~ 'uuid';

        if (has(global_child, pu) && !has(global_parent, pu),
            root = p;
            child_u = global_child:pu;
            h = 1;

            while(child_u != null && h < 20,
                child = _find_player(child_u);
                seat = global_seats:child_u;

                if (root ~ 'sneaking' && h == 1,
                    _promote_first(pu);
                    break();
                );

                if (child == null || seat == null || child ~ 'mount' != seat,
                    _detach_by_uuid(child_u);
                    break();
                );

                modify(seat, 'pos', root ~ 'pos' + [0, 2.5 * h, 0]);
                modify(seat, 'yaw', root ~ 'yaw');

                child_u = global_child:child_u;
                h = h + 1;
            )
        )
    )
);

__on_player_interacts_with_entity(player, entity, hand) -> (
    if (hand == 'mainhand' && entity ~ 'type' == 'player' && player ~ 'holds' == null && player != entity, (
        pu = player ~ 'uuid';
        tu = entity ~ 'uuid';
        player_root = _find_root(pu);
        target_root = _find_root(tu);
        if (player_root == target_root, (
            if (global_debug, print(player, '[FSIT] misma pila, ignorado'))
        ), (
            _attach(player, entity)
        ))
    ))
);

__on_player_changes_dimension(player, from_pos, from_dim, to_pos, to_dim) -> (
    pu = player ~ 'uuid';
    if (has(global_child, pu), _promote_first(pu));
    if (has(global_parent, pu), _detach_by_uuid(pu));
);

toggle_debug() -> (
    global_debug = !global_debug;
    print(str('[FSIT] debug = %s', global_debug))
);

clear_all() -> (
    run('kill @e[tag=fsitseat]');
    global_child = {};
    global_parent = {};
    global_seats = {};
    print('[FSIT] Todo limpio y reseteado')
);
