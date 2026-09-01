__config() -> {
    'stay_loaded' -> true,
    'scope' -> 'global'
};

global_debug = true;

// Diccionarios directos (Linked List)
global_child = {};  // Quién está sentado SOBRE ti (Tu_UUID -> UUID_del_de_arriba)
global_parent = {}; // Sobre quién estás sentado TÚ (Tu_UUID -> UUID_del_de_abajo)
global_seats = {};  // Tu silla (Tu_UUID -> Entidad_ArmorStand)

_find_player(uuid) -> (
    found = null;
    for(player('all'), if(_ ~ 'uuid' == uuid, found = _));
    found
);

__on_start() -> run('kill @e[tag=fsitseat]');

// Función maestra para desconectar a un jugador y que la cadena colapse sola
_detach_by_uuid(ru) -> (
    if (!has(global_parent, ru), return());
    parent_u = global_parent:ru;
    child_u = global_child:ru;

    // 1. Destruimos la silla 
    seat = global_seats:ru;
    if (seat != null, modify(seat, 'remove'));

    // 2. Lo borramos de los registros
    delete(global_seats, ru);
    delete(global_parent, ru);
    delete(global_child, ru);

    // 3. Si había alguien arriba (C), lo conectamos directamente al de abajo (A).
    if (child_u != null,
        global_child:parent_u = child_u;
        global_parent:child_u = parent_u;
    ,
        // Si no había nadie arriba, el de abajo se queda sin hijo
        delete(global_child, parent_u);
    );
);

// Tira a todos los que estén arriba si la base se desconecta
_drop_all_above(ru) -> (
    while(has(global_child, ru),
        _detach_by_uuid(global_child:ru)
    )
);

_attach(rider, base_player) -> (
    ru = rider ~ 'uuid';
    // Si ya estabas montado en otro lado, te bajamos primero
    if (has(global_parent, ru), _detach_by_uuid(ru));

    // Rastrear hasta el jugador más alto de la cadena actual
    top = base_player;
    top_u = top ~ 'uuid';
    
    while(has(global_child, top_u),
        top_u = global_child:top_u;
        top = _find_player(top_u);
    );

    // Crear la silla a 2.5 bloques arriba del que esté en la cima
    seat = spawn('armor_stand', top ~ 'pos' + [0, 2.5, 0]);
    if (seat != null,
        modify(seat, 'nbt_merge', '{Marker:1b, Small:1b, DisabledSlots:4144959, NoBasePlate:1b, NoGravity:1b, Invisible:1b, Tags:["fsitseat"]}');
        run('attribute ' + seat ~ 'uuid' + ' minecraft:generic.scale base set 0.0001');

        if (modify(rider, 'mount', seat),
            // Enlazar al jinete con el jugador de la cima
            global_child:top_u = ru;
            global_parent:ru = top_u;
            global_seats:ru = seat;
            if (global_debug, print(rider, '[FSIT] Montado al tope de la torre'));
        ,
            modify(seat, 'remove')
        )
    )
);

// Motor principal con el bucle FOR corregido a 2 argumentos
__on_tick() -> (
    for(player('all'),
        p = _; // <-- ASIGNACIÓN CORRECTA USANDO '_'
        pu = p ~ 'uuid';
        
        // ¿Este jugador tiene a alguien encima PERO él no está encima de nadie? (Es la base)
        if (has(global_child, pu) && !has(global_parent, pu),
            root = p;
            child_u = global_child:pu;
            h = 1;
            
            while(child_u != null && h < 20,
                child = _find_player(child_u);
                seat = global_seats:child_u;

                // Si la base se agacha (Shift), botamos al primero y los de arriba caen al suelo en cascada.
                if (root ~ 'sneaking' && h == 1,
                    _detach_by_uuid(child_u);
                    break(); 
                );

                // Si el jinete se bajó por su cuenta (Shift) o desapareció
                if (child == null || seat == null || child ~ 'mount' != seat,
                    _detach_by_uuid(child_u);
                    break(); 
                );

                // Forzar posición. Altura dinámica = h * 2.5. Seguimiento suave.
                modify(seat, 'pos', root ~ 'pos' + [0, 2.5 * h, 0]);
                modify(seat, 'yaw', root ~ 'yaw');

                // Subir al siguiente en la torre
                child_u = global_child:child_u;
                h = h + 1;
            )
        )
    )
);

__on_player_interacts_with_entity(player, entity, hand) -> (
    if (hand == 'mainhand' && entity ~ 'type' == 'player' && player ~ 'holds' == null && player != entity,
        pu = player ~ 'uuid';
        
        // Evitar ciclo infinito
        ok = true;
        cur_u = entity ~ 'uuid';
        while(has(global_parent, cur_u),
            if (cur_u == pu, ok = false; break());
            cur_u = global_parent:cur_u;
        );

        if (ok, _attach(player, entity))
    )
);

__on_player_changes_dimension(player, from_pos, from_dim, to_pos, to_dim) -> (
    pu = player ~ 'uuid';
    _drop_all_above(pu);
    _detach_by_uuid(pu);
);

__on_player_leaves(player) -> (
    pu = player ~ 'uuid';
    _drop_all_above(pu);
    _detach_by_uuid(pu);
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